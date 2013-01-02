package com.gempukku.lotro.hall;

import com.gempukku.lotro.AbstractServer;
import com.gempukku.lotro.DateUtils;
import com.gempukku.lotro.chat.ChatRoomMediator;
import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.game.formats.LotroFormatLibrary;
import com.gempukku.lotro.league.LeagueSerieData;
import com.gempukku.lotro.league.LeagueService;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.GameResultListener;
import com.gempukku.lotro.logic.vo.LotroDeck;
import com.gempukku.lotro.tournament.*;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HallServer extends AbstractServer {
    private ChatServer _chatServer;
    private LeagueService _leagueService;
    private TournamentService _tournamentService;
    private LotroCardBlueprintLibrary _library;
    private LotroFormatLibrary _formatLibrary;
    private CollectionsManager _collectionsManager;
    private LotroServer _lotroServer;
    private TournamentPrizeSchemeRegistry _tournamentPrizeSchemeRegistry;

    private CollectionType _allCardsCollectionType = CollectionType.ALL_CARDS;

    private final int _playerInactivityPeriod = 1000 * 20; // 10 seconds

    private int _nextTableId = 1;

    private String _motd;

    private boolean _shutdown;

    private ReadWriteLock _hallDataAccessLock = new ReentrantReadWriteLock(false);

    private Map<String, AwaitingTable> _awaitingTables = new LinkedHashMap<String, AwaitingTable>();
    private Map<String, RunningTable> _runningTables = new LinkedHashMap<String, RunningTable>();

    private Map<Player, HallCommunicationChannel> _playerChannelCommunication = new HashMap<Player, HallCommunicationChannel>();
    private int _nextChannelNumber = 0;

    private Map<String, Tournament> _runningTournaments = new LinkedHashMap<String, Tournament>();
    private Map<String, TournamentQueue> _tournamentQueues = new HashMap<String, TournamentQueue>();
    private final ChatRoomMediator _hallChat;

    public HallServer(LotroServer lotroServer, ChatServer chatServer, LeagueService leagueService, TournamentService tournamentService, LotroCardBlueprintLibrary library,
                      LotroFormatLibrary formatLibrary, CollectionsManager collectionsManager, TournamentPrizeSchemeRegistry tournamentPrizeSchemeRegistry, boolean test) {
        _lotroServer = lotroServer;
        _chatServer = chatServer;
        _leagueService = leagueService;
        _tournamentService = tournamentService;
        _library = library;
        _formatLibrary = formatLibrary;
        _collectionsManager = collectionsManager;
        _tournamentPrizeSchemeRegistry = tournamentPrizeSchemeRegistry;
        _hallChat = _chatServer.createChatRoom("Game Hall", 10);

        _tournamentQueues.put("fotr_queue", new SingleEliminationRecurringQueue(635, "fotr_block",
                CollectionType.MY_CARDS, "fotrQueue-", "Fellowship Block", 8,
                true, tournamentService, _tournamentPrizeSchemeRegistry.getTournamentPrizes("onDemand")));
        _tournamentQueues.put("movie_queue", new SingleEliminationRecurringQueue(635, "movie",
                CollectionType.MY_CARDS, "movieQueue-", "Movie Block", 8,
                true, tournamentService, _tournamentPrizeSchemeRegistry.getTournamentPrizes("onDemand")));
        _tournamentQueues.put("expanded_queue", new SingleEliminationRecurringQueue(635, "expanded",
                CollectionType.MY_CARDS, "expandedQueue-", "Expanded", 8,
                true, tournamentService, _tournamentPrizeSchemeRegistry.getTournamentPrizes("onDemand")));
    }

    @Override
    protected void doAfterStartup() {
        for (Tournament tournament : _tournamentService.getLiveTournaments())
            _runningTournaments.put(tournament.getTournamentId(), tournament);
    }

    public void setShutdown(boolean shutdown) {
        _hallDataAccessLock.writeLock().lock();
        try {
            _shutdown = shutdown;
            if (shutdown) {
                cancelWaitingTables();
                cancelTournamentQueues();
                _chatServer.sendSystemMessageToAllChatRooms("System is entering shutdown mode and will be restarted when all games are finished");
            }
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    public String getMOTD() {
        return _motd;
    }

    public void setMOTD(String motd) {
        _motd = motd;
    }

    public int getTablesCount() {
        _hallDataAccessLock.readLock().lock();
        try {
            return _awaitingTables.size() + _runningTables.size();
        } finally {
            _hallDataAccessLock.readLock().unlock();
        }
    }

    private void cancelWaitingTables() {
        _awaitingTables.clear();
    }

    private void cancelTournamentQueues() {
        for (TournamentQueue tournamentQueue : _tournamentQueues.values())
            tournamentQueue.leaveAllPlayers(_collectionsManager);
    }

    /**
     * @return If table created, otherwise <code>false</code> (if the user already is sitting at a table or playing).
     */
    public void createNewTable(String type, Player player, String deckName) throws HallException {
        if (_shutdown)
            throw new HallException("Server is in shutdown mode. Server will be restarted after all running games are finished.");

        _hallDataAccessLock.writeLock().lock();
        try {
            if (isPlayerBusy(player.getName()))
                throw new HallException("You can't start a table, as you are considered busy");

            League league = null;
            LeagueSerieData leagueSerie = null;
            CollectionType collectionType = _allCardsCollectionType;
            LotroFormat format = _formatLibrary.getHallFormats().get(type);

            if (format == null) {
                // Maybe it's a league format?
                league = _leagueService.getLeagueByType(type);
                if (league != null) {
                    if (!_leagueService.isPlayerInLeague(league, player))
                        throw new HallException("You're not in that league");

                    leagueSerie = _leagueService.getCurrentLeagueSerie(league);
                    if (leagueSerie == null)
                        throw new HallException("There is no ongoing serie for that league");

                    if (!_leagueService.canPlayRankedGame(league, leagueSerie, player.getName()))
                        throw new HallException("You have already played max games in league");
                    format = _formatLibrary.getFormat(leagueSerie.getFormat());
                    collectionType = leagueSerie.getCollectionType();
                }
            }
            // It's not a normal format and also not a league one
            if (format == null)
                throw new HallException("This format is not supported: " + type);

            LotroDeck lotroDeck = validateUserAndDeck(format, player, deckName, collectionType);

            String tableId = String.valueOf(_nextTableId++);
            AwaitingTable table = new AwaitingTable(format, collectionType, league, leagueSerie);
            _awaitingTables.put(tableId, table);

            joinTableInternal(tableId, player.getName(), table, lotroDeck);
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    public boolean joinQueue(String queueId, Player player, String deckName) throws HallException {
        if (_shutdown)
            throw new HallException("Server is in shutdown mode. Server will be restarted after all running games are finished.");

        _hallDataAccessLock.writeLock().lock();
        try {
            if (isPlayerBusy(player.getName()))
                throw new HallException("You can't join a queue, as you are considered busy");

            TournamentQueue tournamentQueue = _tournamentQueues.get(queueId);
            if (tournamentQueue == null)
                throw new HallException("Tournament queue already finished accepting players, try again in a few seconds");
            if (tournamentQueue.isPlayerSignedUp(player.getName()))
                throw new HallException("You have already joined that queue");

            LotroDeck lotroDeck = null;
            if (tournamentQueue.isRequiresDeck())
                lotroDeck = validateUserAndDeck(_formatLibrary.getFormat(tournamentQueue.getFormat()), player, deckName, tournamentQueue.getCollectionType());

            tournamentQueue.joinPlayer(_collectionsManager, player, lotroDeck);

            return true;
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    /**
     * @return If table joined, otherwise <code>false</code> (if the user already is sitting at a table or playing).
     */
    public boolean joinTableAsPlayer(String tableId, Player player, String deckName) throws HallException {
        if (_shutdown)
            throw new HallException("Server is in shutdown mode. Server will be restarted after all running games are finished.");

        _hallDataAccessLock.writeLock().lock();
        try {
            if (isPlayerBusy(player.getName()))
                throw new HallException("You can't join a table, as you are considered busy");

            AwaitingTable awaitingTable = _awaitingTables.get(tableId);
            if (awaitingTable == null)
                throw new HallException("Table is already taken or was removed");

            if (awaitingTable.getLeague() != null && !_leagueService.isPlayerInLeague(awaitingTable.getLeague(), player))
                throw new HallException("You're not in that league");

            LotroDeck lotroDeck = validateUserAndDeck(awaitingTable.getLotroFormat(), player, deckName, awaitingTable.getCollectionType());

            joinTableInternal(tableId, player.getName(), awaitingTable, lotroDeck);

            return true;
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    public void leaveQueues(Player player) {
        _hallDataAccessLock.writeLock().lock();
        try {
            for (TournamentQueue tournamentQueue : _tournamentQueues.values()) {
                if (tournamentQueue.isPlayerSignedUp(player.getName()))
                    tournamentQueue.leavePlayer(_collectionsManager, player);
            }
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    public void leaveAwaitingTables(Player player) {
        _hallDataAccessLock.writeLock().lock();
        try {
            Map<String, AwaitingTable> copy = new HashMap<String, AwaitingTable>(_awaitingTables);
            for (Map.Entry<String, AwaitingTable> table : copy.entrySet()) {
                if (table.getValue().hasPlayer(player.getName())) {
                    boolean empty = table.getValue().removePlayer(player.getName());
                    if (empty)
                        _awaitingTables.remove(table.getKey());
                }
            }
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    public boolean processTournament(String tournamentId, Player player, TournamentVisitor tournamentVisitor) {
        _hallDataAccessLock.readLock().lock();
        try {
            Tournament tournament = _runningTournaments.get(tournamentId);
            if (tournament == null)
                return false;

            return true;
        } finally {
            _hallDataAccessLock.readLock().unlock();
        }
    }

    public void signupUserForHall(Player player, HallChannelVisitor hallChannelVisitor) {
        _hallDataAccessLock.readLock().lock();
        try {
            HallCommunicationChannel channel = new HallCommunicationChannel(_nextChannelNumber++);
            channel.processCommunicationChannel(this, player, hallChannelVisitor);
            _playerChannelCommunication.put(player, channel);
        } finally {
            _hallDataAccessLock.readLock().unlock();
        }
    }

    public void processHall(Player player, int channelNumber, HallChannelVisitor hallChannelVisitor) {
        _hallDataAccessLock.readLock().lock();
        try {
            HallCommunicationChannel communicationChannel = _playerChannelCommunication.get(player);
            if (communicationChannel != null) {
                if (communicationChannel.getChannelNumber() == channelNumber) {
                    communicationChannel.processCommunicationChannel(this, player, hallChannelVisitor);
                } else {
                    throw new WebApplicationException(Response.Status.CONFLICT);
                }
            } else {
                throw new WebApplicationException(Response.Status.GONE);
            }
        } finally {
            _hallDataAccessLock.readLock().unlock();
        }
    }

    protected void processHall(Player player, HallInfoVisitor visitor) {
        _hallDataAccessLock.readLock().lock();
        try {
            visitor.serverTime(DateUtils.getStringDateWithHour());
            visitor.playerBusy(isPlayerBusy(player.getName()));

            // First waiting
            for (Map.Entry<String, AwaitingTable> tableInformation : _awaitingTables.entrySet()) {
                final AwaitingTable table = tableInformation.getValue();

                List<String> players;
                if (table.getLeague() != null)
                    players = Collections.<String>emptyList();
                else
                    players = table.getPlayerNames();
                visitor.visitTable(tableInformation.getKey(), null, false, HallInfoVisitor.TableStatus.WAITING, "Waiting", table.getLotroFormat().getName(), getTournamentName(table), players, null);
            }

            // Then non-finished
            Map<String, RunningTable> finishedTables = new HashMap<String, RunningTable>();

            for (Map.Entry<String, RunningTable> runningGame : _runningTables.entrySet()) {
                final RunningTable runningTable = runningGame.getValue();
                LotroGameMediator lotroGameMediator = _lotroServer.getGameById(runningTable.getGameId());
                if (lotroGameMediator != null) {
                    if (!lotroGameMediator.isFinished())
                        visitor.visitTable(runningGame.getKey(), runningTable.getGameId(), player.getType().contains("a") || lotroGameMediator.isAllowSpectators(), HallInfoVisitor.TableStatus.PLAYING, lotroGameMediator.getGameStatus(), runningTable.getFormatName(), runningTable.getTournamentName(), lotroGameMediator.getPlayersPlaying(), lotroGameMediator.getWinner());
                    else
                        finishedTables.put(runningGame.getKey(), runningTable);
                }
            }

            // Then rest
            for (Map.Entry<String, RunningTable> nonPlayingGame : finishedTables.entrySet()) {
                final RunningTable runningTable = nonPlayingGame.getValue();
                LotroGameMediator lotroGameMediator = _lotroServer.getGameById(runningTable.getGameId());
                if (lotroGameMediator != null)
                    visitor.visitTable(nonPlayingGame.getKey(), runningTable.getGameId(), false, HallInfoVisitor.TableStatus.FINISHED, lotroGameMediator.getGameStatus(), runningTable.getFormatName(), runningTable.getTournamentName(), lotroGameMediator.getPlayersPlaying(), lotroGameMediator.getWinner());
            }

            for (Map.Entry<String, TournamentQueue> tournamentQueueEntry : _tournamentQueues.entrySet()) {
                String tournamentQueueKey = tournamentQueueEntry.getKey();
                TournamentQueue tournamentQueue = tournamentQueueEntry.getValue();
                visitor.visitTournamentQueue(tournamentQueueKey, tournamentQueue.getCost(), tournamentQueue.getCollectionType().getFullName(),
                        _formatLibrary.getFormat(tournamentQueue.getFormat()).getName(), tournamentQueue.getTournamentQueueName(),
                        tournamentQueue.getPrizesDescription(), tournamentQueue.getPairingDescription(), tournamentQueue.getStartCondition(),
                        tournamentQueue.getPlayerCount(), tournamentQueue.isPlayerSignedUp(player.getName()));
            }

            String gameId = getPlayingPlayerGameId(player.getName());
            if (gameId != null)
                visitor.runningPlayerGame(gameId);
        } finally {
            _hallDataAccessLock.readLock().unlock();
        }
    }

    private LotroDeck validateUserAndDeck(LotroFormat format, Player player, String deckName, CollectionType collectionType) throws HallException {
        LotroDeck lotroDeck = _lotroServer.getParticipantDeck(player, deckName);
        if (lotroDeck == null)
            throw new HallException("You don't have a deck registered yet");

        try {
            format.validateDeck(lotroDeck);
        } catch (DeckInvalidException e) {
            throw new HallException("Your selected deck is not valid for this format: " + e.getMessage());
        }

        // Now check if player owns all the cards
        if (collectionType.getCode().equals("default")) {
            CardCollection ownedCollection = _collectionsManager.getPlayerCollection(player, "permanent");

            LotroDeck filteredSpecialCardsDeck = new LotroDeck(lotroDeck.getDeckName());
            filteredSpecialCardsDeck.setRing(filterCard(lotroDeck.getRing(), ownedCollection));
            filteredSpecialCardsDeck.setRingBearer(filterCard(lotroDeck.getRingBearer(), ownedCollection));

            for (String site : lotroDeck.getSites())
                filteredSpecialCardsDeck.addSite(filterCard(site, ownedCollection));

            for (Map.Entry<String, Integer> cardCount : CollectionUtils.getTotalCardCount(lotroDeck.getAdventureCards()).entrySet()) {
                String blueprintId = cardCount.getKey();
                int count = cardCount.getValue();

                int owned = 0;
                if (ownedCollection != null)
                    owned = ownedCollection.getItemCount(blueprintId);
                int fromOwned = Math.min(owned, count);

                for (int i = 0; i < fromOwned; i++)
                    filteredSpecialCardsDeck.addCard(blueprintId);
                if (count - fromOwned > 0) {
                    String baseBlueprintId = _library.getBaseBlueprintId(blueprintId);
                    for (int i = 0; i < (count - fromOwned); i++)
                        filteredSpecialCardsDeck.addCard(baseBlueprintId);
                }
            }

            lotroDeck = filteredSpecialCardsDeck;
        } else {
            CardCollection collection = _collectionsManager.getPlayerCollection(player, collectionType.getCode());
            if (collection == null)
                throw new HallException("You don't have cards in the required collection to play in this format");

            Map<String, Integer> deckCardCounts = CollectionUtils.getTotalCardCountForDeck(lotroDeck);

            for (Map.Entry<String, Integer> cardCount : deckCardCounts.entrySet()) {
                final int collectionCount = collection.getItemCount(cardCount.getKey());
                if (collectionCount < cardCount.getValue()) {
                    String cardName = GameUtils.getFullName(_library.getLotroCardBlueprint(cardCount.getKey()));
                    throw new HallException("You don't have the required cards in collection: " + cardName + " required " + cardCount.getValue() + ", owned " + collectionCount);
                }
            }
        }

        return lotroDeck;
    }

    private String filterCard(String blueprintId, CardCollection ownedCollection) {
        if (ownedCollection == null || ownedCollection.getItemCount(blueprintId) == 0)
            return _library.getBaseBlueprintId(blueprintId);
        return blueprintId;
    }

    private String getTournamentName(AwaitingTable table) {
        String tournamentName = "Casual";
        final League league = table.getLeague();
        if (league != null)
            tournamentName = league.getName() + " - " + table.getLeagueSerie().getName();
        return tournamentName;
    }

    private void createGameFromAwaitingTable(String tableId, AwaitingTable awaitingTable) {
        Set<LotroGameParticipant> players = awaitingTable.getPlayers();
        LotroGameParticipant[] participants = players.toArray(new LotroGameParticipant[players.size()]);
        final League league = awaitingTable.getLeague();
        final LeagueSerieData leagueSerie = awaitingTable.getLeagueSerie();

        GameResultListener listener = null;
        if (league != null) {
            listener = new GameResultListener() {
                @Override
                public void gameFinished(String winnerPlayerId, String winReason, Map<String, String> loserPlayerIdsWithReasons) {
                    _leagueService.reportLeagueGameResult(league, leagueSerie, winnerPlayerId, loserPlayerIdsWithReasons.keySet().iterator().next());
                }

                @Override
                public void gameCancelled() {
                    // Do nothing...
                }
            };
        }

        createGame(tableId, participants, listener, awaitingTable.getLotroFormat(), getTournamentName(awaitingTable), true, league == null, false, league != null);
        _awaitingTables.remove(tableId);
    }

    private void createGame(String tableId, LotroGameParticipant[] participants, GameResultListener listener, LotroFormat lotroFormat, String tournamentName, boolean allowSpectators, boolean allowCancelling, boolean muteSpectators, boolean competitiveTime) {
        String gameId = _lotroServer.createNewGame(lotroFormat, tournamentName, participants, allowSpectators, allowCancelling, muteSpectators, competitiveTime);
        LotroGameMediator lotroGameMediator = _lotroServer.getGameById(gameId);
        if (listener != null)
            lotroGameMediator.addGameResultListener(listener);
        lotroGameMediator.startGame();
        _runningTables.put(tableId, new RunningTable(gameId, lotroFormat.getName(), tournamentName));
    }

    private void joinTableInternal(String tableId, String player, AwaitingTable awaitingTable, LotroDeck lotroDeck) throws HallException {
        League league = awaitingTable.getLeague();
        if (league != null) {
            LeagueSerieData leagueSerie = awaitingTable.getLeagueSerie();
            if (!_leagueService.canPlayRankedGame(league, leagueSerie, player))
                throw new HallException("You have already played max games in league");
            if (awaitingTable.getPlayerNames().size() != 0 && !_leagueService.canPlayRankedGameAgainst(league, leagueSerie, awaitingTable.getPlayerNames().iterator().next(), player))
                throw new HallException("You have already played ranked league game against this player in that series");
        }
        boolean tableFull = awaitingTable.addPlayer(new LotroGameParticipant(player, lotroDeck));
        if (tableFull)
            createGameFromAwaitingTable(tableId, awaitingTable);
    }

    private String getPlayingPlayerGameId(String playerId) {
        for (Map.Entry<String, RunningTable> runningTable : _runningTables.entrySet()) {
            String gameId = runningTable.getValue().getGameId();
            LotroGameMediator lotroGameMediator = _lotroServer.getGameById(gameId);
            if (lotroGameMediator != null && !lotroGameMediator.isFinished() && lotroGameMediator.getPlayersPlaying().contains(playerId))
                return gameId;
        }

        return null;
    }

    private boolean isPlayerBusy(String playerId) {
        for (AwaitingTable awaitingTable : _awaitingTables.values())
            if (awaitingTable.hasPlayer(playerId))
                return true;

        for (RunningTable runningTable : _runningTables.values()) {
            String gameId = runningTable.getGameId();
            LotroGameMediator lotroGameMediator = _lotroServer.getGameById(gameId);
            if (lotroGameMediator != null && !lotroGameMediator.isFinished() && lotroGameMediator.getPlayersPlaying().contains(playerId))
                return true;
        }

        for (TournamentQueue tournamentQueue : _tournamentQueues.values()) {
            if (tournamentQueue.isPlayerSignedUp(playerId))
                return true;
        }

        for (Tournament runningTournament : _runningTournaments.values()) {
            if (runningTournament.isPlayerInCompetition(playerId))
                return true;
        }

        return false;
    }

    @Override
    protected void cleanup() {
        _hallDataAccessLock.writeLock().lock();
        try {
            // Remove finished games
            HashMap<String, RunningTable> copy = new HashMap<String, RunningTable>(_runningTables);
            for (Map.Entry<String, RunningTable> runningTable : copy.entrySet()) {
                if (_lotroServer.getGameById(runningTable.getValue().getGameId()) == null)
                    _runningTables.remove(runningTable.getKey());
            }

            long currentTime = System.currentTimeMillis();
            Map<Player, HallCommunicationChannel> visitCopy = new LinkedHashMap<Player, HallCommunicationChannel>(_playerChannelCommunication);
            for (Map.Entry<Player, HallCommunicationChannel> lastVisitedPlayer : visitCopy.entrySet()) {
                if (currentTime > lastVisitedPlayer.getValue().getLastConsumed() + _playerInactivityPeriod) {
                    Player player = lastVisitedPlayer.getKey();
                    _playerChannelCommunication.remove(player);
                    leaveAwaitingTables(player);
                    leaveQueues(player);
                }
            }

            for (Map.Entry<String, TournamentQueue> runningTournamentQueue : new HashMap<String, TournamentQueue>(_tournamentQueues).entrySet()) {
                String tournamentQueueKey = runningTournamentQueue.getKey();
                TournamentQueue tournamentQueue = runningTournamentQueue.getValue();
                HallTournamentQueueCallback queueCallback = new HallTournamentQueueCallback();
                // If it's finished, remove it
                if (tournamentQueue.process(queueCallback))
                    _tournamentQueues.remove(tournamentQueueKey);
            }

            for (Tournament runningTournament : new ArrayList<Tournament>(_runningTournaments.values())) {
                runningTournament.advanceTournament(new HallTournamentCallback(runningTournament), _collectionsManager);
                if (runningTournament.getTournamentStage() == Tournament.Stage.FINISHED)
                    _runningTournaments.remove(runningTournament);
            }

        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    private class HallTournamentQueueCallback implements TournamentQueueCallback {
        @Override
        public void createTournament(Tournament tournament) {
            _runningTournaments.put(tournament.getTournamentId(), tournament);
        }
    }

    private class HallTournamentCallback implements TournamentCallback {
        private Tournament _tournament;

        private HallTournamentCallback(Tournament tournament) {
            _tournament = tournament;
        }

        @Override
        public void createGame(String playerOne, LotroDeck deckOne, String playerTwo, LotroDeck deckTwo, boolean allowSpectators) {
            final LotroGameParticipant[] participants = new LotroGameParticipant[2];
            participants[0] = new LotroGameParticipant(playerOne, deckOne);
            participants[1] = new LotroGameParticipant(playerTwo, deckTwo);
            createGameInternal(participants, allowSpectators);
        }

        private void createGameInternal(final LotroGameParticipant[] participants, final boolean allowSpectators) {
            _hallDataAccessLock.writeLock().lock();
            try {
                if (!_shutdown) {
                    HallServer.this.createGame(String.valueOf(_nextTableId++), participants,
                            new GameResultListener() {
                                @Override
                                public void gameFinished(String winnerPlayerId, String winReason, Map<String, String> loserPlayerIdsWithReasons) {
                                    _tournament.reportGameFinished(winnerPlayerId, loserPlayerIdsWithReasons.keySet().iterator().next());
                                }

                                @Override
                                public void gameCancelled() {
                                    createGameInternal(participants, allowSpectators);
                                }
                            }, _formatLibrary.getFormat(_tournament.getFormat()), _tournament.getTournamentName(), allowSpectators, false, true, true);
                }
            } finally {
                _hallDataAccessLock.writeLock().unlock();
            }
        }

        @Override
        public void broadcastMessage(String message) {
            _hallChat.sendMessage("TournamentSystem", message, true);
        }
    }
}
