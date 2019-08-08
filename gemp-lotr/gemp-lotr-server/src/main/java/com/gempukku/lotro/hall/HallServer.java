package com.gempukku.lotro.hall;

import com.gempukku.lotro.AbstractServer;
import com.gempukku.lotro.DateUtils;
import com.gempukku.lotro.PrivateInformationException;
import com.gempukku.lotro.SubscriptionConflictException;
import com.gempukku.lotro.SubscriptionExpiredException;
import com.gempukku.lotro.cards.CardSets;
import com.gempukku.lotro.chat.ChatCommandCallback;
import com.gempukku.lotro.chat.ChatCommandErrorException;
import com.gempukku.lotro.chat.ChatRoomMediator;
import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.draft.Draft;
import com.gempukku.lotro.draft.DraftChannelVisitor;
import com.gempukku.lotro.draft.DraftFinishedException;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.CollectionUtils;
import com.gempukku.lotro.game.DeckInvalidException;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.LotroFormat;
import com.gempukku.lotro.game.LotroGameMediator;
import com.gempukku.lotro.game.LotroGameParticipant;
import com.gempukku.lotro.game.LotroServer;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.game.formats.LotroFormatLibrary;
import com.gempukku.lotro.league.LeagueSerieData;
import com.gempukku.lotro.league.LeagueService;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.GameResultListener;
import com.gempukku.lotro.logic.vo.LotroDeck;
import com.gempukku.lotro.service.AdminService;
import com.gempukku.lotro.tournament.ImmediateRecurringQueue;
import com.gempukku.lotro.tournament.PairingMechanismRegistry;
import com.gempukku.lotro.tournament.RecurringScheduledQueue;
import com.gempukku.lotro.tournament.ScheduledTournamentQueue;
import com.gempukku.lotro.tournament.Tournament;
import com.gempukku.lotro.tournament.TournamentCallback;
import com.gempukku.lotro.tournament.TournamentPrizeSchemeRegistry;
import com.gempukku.lotro.tournament.TournamentQueue;
import com.gempukku.lotro.tournament.TournamentQueueCallback;
import com.gempukku.lotro.tournament.TournamentQueueInfo;
import com.gempukku.lotro.tournament.TournamentService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HallServer extends AbstractServer {
    private final int _playerInactivityPeriod = 1000 * 20; // 20 seconds
    private final long _scheduledTournamentLoadTime = 1000 * 60 * 60 * 24 * 7; // Week
    // Repeat tournaments every 2 days
    private final long _repeatTournaments = 1000 * 60 * 60 * 24 * 2;

    private ChatServer _chatServer;
    private LeagueService _leagueService;
    private TournamentService _tournamentService;
    private LotroCardBlueprintLibrary _library;
    private LotroFormatLibrary _formatLibrary;
    private CollectionsManager _collectionsManager;
    private LotroServer _lotroServer;
    private PairingMechanismRegistry _pairingMechanismRegistry;
    private CardSets _cardSets;
    private AdminService _adminService;
    private TournamentPrizeSchemeRegistry _tournamentPrizeSchemeRegistry;

    private CollectionType _defaultCollectionType = CollectionType.ALL_CARDS;
    private CollectionType _tournamentCollectionType = CollectionType.OWNED_TOURNAMENT_CARDS;

    private int _nextTableId = 1;

    private String _motd;

    private boolean _shutdown;

    private ReadWriteLock _hallDataAccessLock = new ReentrantReadWriteLock(false);

    private Map<String, AwaitingTable> _awaitingTables = new LinkedHashMap<String, AwaitingTable>();
    private Map<String, RunningTable> _runningTables = new LinkedHashMap<String, RunningTable>();

    private Map<Player, HallCommunicationChannel> _playerChannelCommunication = new ConcurrentHashMap<Player, HallCommunicationChannel>();
    private int _nextChannelNumber = 0;

    private Map<String, Tournament> _runningTournaments = new LinkedHashMap<String, Tournament>();

    private Map<String, TournamentQueue> _tournamentQueues = new LinkedHashMap<String, TournamentQueue>();
    private final ChatRoomMediator _hallChat;
    private final GameResultListener _notifyHallListeners = new NotifyHallListenersGameResultListener();

    // 5 minutes timeout, 40 minutes per game per player
    private GameTimer competitiveTimer = new GameTimer(false, "Competitive", 60 * 40, 60 * 5);

    public HallServer(LotroServer lotroServer, ChatServer chatServer, LeagueService leagueService, TournamentService tournamentService, LotroCardBlueprintLibrary library,
                      LotroFormatLibrary formatLibrary, CollectionsManager collectionsManager,
                      AdminService adminService,
                      TournamentPrizeSchemeRegistry tournamentPrizeSchemeRegistry,
                      PairingMechanismRegistry pairingMechanismRegistry, CardSets cardSets) {
        _lotroServer = lotroServer;
        _chatServer = chatServer;
        _leagueService = leagueService;
        _tournamentService = tournamentService;
        _library = library;
        _formatLibrary = formatLibrary;
        _collectionsManager = collectionsManager;
        _adminService = adminService;
        _tournamentPrizeSchemeRegistry = tournamentPrizeSchemeRegistry;
        _pairingMechanismRegistry = pairingMechanismRegistry;
        _cardSets = cardSets;

        _hallChat = _chatServer.createChatRoom("Game Hall", true, 15);
        _hallChat.addChatCommandCallback("ban",
                new ChatCommandCallback() {
                    @Override
                    public void commandReceived(String from, String parameters, boolean admin) throws ChatCommandErrorException {
                        if (admin) {
                            _adminService.banUser(parameters.trim());
                        } else {
                            throw new ChatCommandErrorException("Only administrator can ban users");
                        }
                    }
                });
        _hallChat.addChatCommandCallback("banIp",
                new ChatCommandCallback() {
                    @Override
                    public void commandReceived(String from, String parameters, boolean admin) throws ChatCommandErrorException {
                        if (admin) {
                            _adminService.banIp(parameters.trim());
                        } else {
                            throw new ChatCommandErrorException("Only administrator can ban users");
                        }
                    }
                });
        _hallChat.addChatCommandCallback("banIpRange",
                new ChatCommandCallback() {
                    @Override
                    public void commandReceived(String from, String parameters, boolean admin) throws ChatCommandErrorException {
                        if (admin) {
                            _adminService.banIpPrefix(parameters.trim());
                        } else {
                            throw new ChatCommandErrorException("Only administrator can ban users");
                        }
                    }
                });

        _tournamentQueues.put("fotr_queue", new ImmediateRecurringQueue(1000, "fotr_block",
                CollectionType.ALL_CARDS, "fotrQueue-", "Fellowship Block", 8,
                true, tournamentService, _tournamentPrizeSchemeRegistry.getTournamentPrizes(cardSets, "onDemand"), _pairingMechanismRegistry.getPairingMechanism("singleElimination")));
        _tournamentQueues.put("movie_queue", new ImmediateRecurringQueue(1000, "movie",
                CollectionType.ALL_CARDS, "movieQueue-", "Movie Block", 8,
                true, tournamentService, _tournamentPrizeSchemeRegistry.getTournamentPrizes(cardSets, "onDemand"), _pairingMechanismRegistry.getPairingMechanism("singleElimination")));
        _tournamentQueues.put("expanded_queue", new ImmediateRecurringQueue(1000, "expanded",
                CollectionType.ALL_CARDS, "expandedQueue-", "Expanded", 8,
                true, tournamentService, _tournamentPrizeSchemeRegistry.getTournamentPrizes(cardSets, "onDemand"), _pairingMechanismRegistry.getPairingMechanism("singleElimination")));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            _tournamentQueues.put("fotr_daily_eu", new RecurringScheduledQueue(sdf.parse("2013-01-15 19:30:00").getTime(), _repeatTournaments, "fotrDailyEu-", "Daily Gondor Fellowship Block", 0,
                    true, _tournamentCollectionType, tournamentService, _tournamentPrizeSchemeRegistry.getTournamentPrizes(cardSets, "daily"), _pairingMechanismRegistry.getPairingMechanism("swiss-3"),
                    "fotr_block", 4));
            _tournamentQueues.put("fotr_daily_us", new RecurringScheduledQueue(sdf.parse("2013-01-16 00:30:00").getTime(), _repeatTournaments, "fotrDailyUs-", "Daily Rohan Fellowship Block", 0,
                    true, _tournamentCollectionType, tournamentService, _tournamentPrizeSchemeRegistry.getTournamentPrizes(cardSets, "daily"), _pairingMechanismRegistry.getPairingMechanism("swiss-3"),
                    "fotr_block", 4));
            _tournamentQueues.put("movie_daily_eu", new RecurringScheduledQueue(sdf.parse("2013-01-16 19:30:00").getTime(), _repeatTournaments, "movieDailyEu-", "Daily Gondor Movie Block", 0,
                    true, _tournamentCollectionType, tournamentService, _tournamentPrizeSchemeRegistry.getTournamentPrizes(cardSets, "daily"), _pairingMechanismRegistry.getPairingMechanism("swiss-3"),
                    "movie", 4));
            _tournamentQueues.put("movie_daily_us", new RecurringScheduledQueue(sdf.parse("2013-01-17 00:30:00").getTime(), _repeatTournaments, "movieDailyUs-", "Daily Rohan Movie Block", 0,
                    true, _tournamentCollectionType, tournamentService, _tournamentPrizeSchemeRegistry.getTournamentPrizes(cardSets, "daily"), _pairingMechanismRegistry.getPairingMechanism("swiss-3"),
                    "movie", 4));
        } catch (ParseException exp) {
            // Ignore, can't happen
        }
    }

    private void hallChanged() {
        for (HallCommunicationChannel hallCommunicationChannel : _playerChannelCommunication.values())
            hallCommunicationChannel.hallChanged();
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
                hallChanged();
            }
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    public void setMOTD(String motd) {
        _hallDataAccessLock.writeLock().lock();
        try {
            _motd = motd;
            hallChanged();
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    public int getTablesCount() {
        _hallDataAccessLock.readLock().lock();
        try {
            return _runningTables.size();
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
    public void createNewTable(String type, Player player, String deckName, String timer) throws HallException {
        if (_shutdown)
            throw new HallException("Server is in shutdown mode. Server will be restarted after all running games are finished.");

        _hallDataAccessLock.writeLock().lock();
        try {
            League league = null;
            LeagueSerieData leagueSerie = null;
            CollectionType collectionType = _defaultCollectionType;
            LotroFormat format = _formatLibrary.getHallFormats().get(type);
            GameTimer gameTimer = resolveTimer(timer);

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

                    verifyNotPlayingLeagueGame(player, league);

                    gameTimer = competitiveTimer;
                }
            }
            // It's not a normal format and also not a league one
            if (format == null)
                throw new HallException("This format is not supported: " + type);

            LotroDeck lotroDeck = validateUserAndDeck(format, player, deckName, collectionType, league != null);

            String tableId = String.valueOf(_nextTableId++);

            GameSettings gameSettings = new GameSettings(collectionType, format, league, leagueSerie,
                    league != null, gameTimer.isLongGame(), gameTimer.getName(), gameTimer.getMaxSecondsPerPlayer(), gameTimer.getMaxSecondsPerDecision());

            AwaitingTable table = new AwaitingTable(gameSettings);
            _awaitingTables.put(tableId, table);

            joinTableInternal(tableId, player.getName(), table, lotroDeck);
            hallChanged();
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    private GameTimer resolveTimer(String timer) {
        if (timer != null) {
            if (timer.equals("default")) {
                return new GameTimer(false, "Default", 60 * 80, 60 * 5);
            } else if (timer.equals("blitz")) {
                return new GameTimer(false, "Blitz!", 60 * 30, 60 * 2);
            } else if (timer.equals("slow")) {
                return new GameTimer(false, "Slow", 60 * 120, 60 * 5);
            } else if (timer.equals("glacial")) {
                return new GameTimer(false, "Glacial", 60 * 60 * 24 * 3, 60 * 60 * 24);
            }
        }
        return new GameTimer(false, "Default", 60 * 80, 60 * 5);
    }

    private void verifyNotPlayingLeagueGame(Player player, League league) throws HallException {
        for (AwaitingTable awaitingTable : _awaitingTables.values()) {
            if (league.equals(awaitingTable.getGameSettings().getLeague())
                    && awaitingTable.hasPlayer(player.getName())) {
                throw new HallException("You can't play in multiple league games at the same time");
            }
        }

        for (RunningTable runningTable : _runningTables.values()) {
            if (league.equals(runningTable.getLeague())) {
                LotroGameMediator game = runningTable.getLotroGameMediator();
                if (game != null && !game.isFinished() && game.getPlayersPlaying().contains(player.getName()))
                    throw new HallException("You can't play in multiple league games at the same time");
            }
        }
    }

    public boolean joinQueue(String queueId, Player player, String deckName) throws HallException {
        if (_shutdown)
            throw new HallException("Server is in shutdown mode. Server will be restarted after all running games are finished.");

        _hallDataAccessLock.writeLock().lock();
        try {
            TournamentQueue tournamentQueue = _tournamentQueues.get(queueId);
            if (tournamentQueue == null)
                throw new HallException("Tournament queue already finished accepting players, try again in a few seconds");
            if (tournamentQueue.isPlayerSignedUp(player.getName()))
                throw new HallException("You have already joined that queue");

            LotroDeck lotroDeck = null;
            if (tournamentQueue.isRequiresDeck())
                lotroDeck = validateUserAndDeck(_formatLibrary.getFormat(tournamentQueue.getFormat()), player, deckName, tournamentQueue.getCollectionType(), true);

            tournamentQueue.joinPlayer(_collectionsManager, player, lotroDeck);

            hallChanged();

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
            AwaitingTable awaitingTable = _awaitingTables.get(tableId);
            if (awaitingTable == null)
                throw new HallException("Table is already taken or was removed");

            if (awaitingTable.hasPlayer(player.getName()))
                throw new HallException("You can't play against yourself");

            final League league = awaitingTable.getGameSettings().getLeague();
            if (league != null) {
                if (!_leagueService.isPlayerInLeague(league, player))
                    throw new HallException("You're not in that league");

                verifyNotPlayingLeagueGame(player, league);
            }

            LotroDeck lotroDeck = validateUserAndDeck(awaitingTable.getGameSettings().getLotroFormat(), player, deckName, awaitingTable.getGameSettings().getCollectionType(), league != null);

            joinTableInternal(tableId, player.getName(), awaitingTable, lotroDeck);

            hallChanged();

            return true;
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    public void leaveQueue(String queueId, Player player) {
        _hallDataAccessLock.writeLock().lock();
        try {
            TournamentQueue tournamentQueue = _tournamentQueues.get(queueId);
            if (tournamentQueue != null && tournamentQueue.isPlayerSignedUp(player.getName())) {
                tournamentQueue.leavePlayer(_collectionsManager, player);
                hallChanged();
            }
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    private boolean leaveQueuesForLeavingPlayer(Player player) {
        _hallDataAccessLock.writeLock().lock();
        try {
            boolean result = false;
            for (TournamentQueue tournamentQueue : _tournamentQueues.values()) {
                if (tournamentQueue.isPlayerSignedUp(player.getName())) {
                    tournamentQueue.leavePlayer(_collectionsManager, player);
                    result = true;
                }
            }
            return result;
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    public void dropFromTournament(String tournamentId, Player player) {
        _hallDataAccessLock.writeLock().lock();
        try {
            Tournament tournament = _runningTournaments.get(tournamentId);
            if (tournament != null) {
                tournament.dropPlayer(player.getName());
                hallChanged();
            }
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    public void singupForDraft(String tournamentId, Player player, DraftChannelVisitor draftChannelVisitor)
            throws DraftFinishedException {
        _hallDataAccessLock.readLock().lock();
        try {
            Tournament tournament = _runningTournaments.get(tournamentId);
            if (tournament == null)
                throw new DraftFinishedException();
            Draft draft = tournament.getDraft();
            if (draft == null)
                throw new DraftFinishedException();
            draft.signUpForDraft(player.getName(), draftChannelVisitor);
        } finally {
            _hallDataAccessLock.readLock().unlock();
        }
    }

    public Draft getDraft(String tournamentId) throws DraftFinishedException {
        _hallDataAccessLock.readLock().lock();
        try {
            Tournament tournament = _runningTournaments.get(tournamentId);
            if (tournament == null)
                throw new DraftFinishedException();
            Draft draft = tournament.getDraft();
            if (draft == null)
                throw new DraftFinishedException();
            return draft;
        } finally {
            _hallDataAccessLock.readLock().unlock();
        }
    }

    public void submitTournamentDeck(String tournamentId, Player player, LotroDeck lotroDeck)
            throws HallException {
        _hallDataAccessLock.readLock().lock();
        try {
            Tournament tournament = _runningTournaments.get(tournamentId);
            if (tournament == null)
                throw new HallException("Tournament no longer accepts deck");
            LotroFormat format = _formatLibrary.getFormat(tournament.getFormat());

            try {
                validateUserAndDeck(format, player, tournament.getCollectionType(), lotroDeck, true);

                tournament.playerSummittedDeck(player.getName(), lotroDeck);
            } catch (DeckInvalidException exp) {
                throw new HallException("Your deck is not valid in the tournament format: " + exp.getMessage());
            }
        } finally {
            _hallDataAccessLock.readLock().unlock();
        }
    }

    public void leaveAwaitingTable(Player player, String tableId) {
        _hallDataAccessLock.writeLock().lock();
        try {
            AwaitingTable table = _awaitingTables.get(tableId);
            if (table != null && table.hasPlayer(player.getName())) {
                boolean empty = table.removePlayer(player.getName());
                if (empty)
                    _awaitingTables.remove(tableId);
                hallChanged();
            }
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    public boolean leaveAwaitingTablesForLeavingPlayer(Player player) {
        _hallDataAccessLock.writeLock().lock();
        try {
            boolean result = false;
            Map<String, AwaitingTable> copy = new HashMap<String, AwaitingTable>(_awaitingTables);
            for (Map.Entry<String, AwaitingTable> table : copy.entrySet()) {
                if (table.getValue().hasPlayer(player.getName())) {
                    boolean empty = table.getValue().removePlayer(player.getName());
                    if (empty)
                        _awaitingTables.remove(table.getKey());
                    result = true;
                }
            }
            return result;
        } finally {
            _hallDataAccessLock.writeLock().unlock();
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

    public HallCommunicationChannel getCommunicationChannel(Player player, int channelNumber) throws SubscriptionExpiredException, SubscriptionConflictException {
        _hallDataAccessLock.readLock().lock();
        try {
            HallCommunicationChannel communicationChannel = _playerChannelCommunication.get(player);
            if (communicationChannel != null) {
                if (communicationChannel.getChannelNumber() == channelNumber) {
                    return communicationChannel;
                } else {
                    throw new SubscriptionConflictException();
                }
            } else {
                throw new SubscriptionExpiredException();
            }
        } finally {
            _hallDataAccessLock.readLock().unlock();
        }
    }

    protected void processHall(Player player, HallInfoVisitor visitor) {
        _hallDataAccessLock.readLock().lock();
        try {
            visitor.serverTime(DateUtils.getStringDateWithHour());
            if (_motd != null)
                visitor.motd(_motd);

            // First waiting
            for (Map.Entry<String, AwaitingTable> tableInformation : _awaitingTables.entrySet()) {
                final AwaitingTable table = tableInformation.getValue();

                List<String> players;
                if (table.getGameSettings().getLeague() != null)
                    players = Collections.<String>emptyList();
                else
                    players = table.getPlayerNames();
                visitor.visitTable(tableInformation.getKey(), null, false, HallInfoVisitor.TableStatus.WAITING, "Waiting", table.getGameSettings().getLotroFormat().getName(), getTournamentName(table), players, table.getPlayerNames().contains(player.getName()), null);
            }

            // Then non-finished
            Map<String, RunningTable> finishedTables = new HashMap<String, RunningTable>();

            final boolean isAdmin = player.getType().contains("a");

            for (Map.Entry<String, RunningTable> runningGame : _runningTables.entrySet()) {
                final RunningTable runningTable = runningGame.getValue();
                LotroGameMediator lotroGameMediator = runningTable.getLotroGameMediator();
                if (lotroGameMediator != null) {
                    if (isAdmin || lotroGameMediator.isVisibleToUser(player.getName())) {
                        if (!lotroGameMediator.isFinished()) {
                            visitor.visitTable(runningGame.getKey(), lotroGameMediator.getGameId(), isAdmin || lotroGameMediator.isAllowSpectators(), HallInfoVisitor.TableStatus.PLAYING, lotroGameMediator.getGameStatus(), runningTable.getFormatName(), runningTable.getTournamentName(), lotroGameMediator.getPlayersPlaying(), lotroGameMediator.getPlayersPlaying().contains(player.getName()), lotroGameMediator.getWinner());
                        } else
                            finishedTables.put(runningGame.getKey(), runningTable);

                        if (!lotroGameMediator.isFinished() && lotroGameMediator.getPlayersPlaying().contains(player.getName()))
                            visitor.runningPlayerGame(lotroGameMediator.getGameId());
                    }
                }
            }

            // Then rest
            for (Map.Entry<String, RunningTable> nonPlayingGame : finishedTables.entrySet()) {
                final RunningTable runningTable = nonPlayingGame.getValue();
                LotroGameMediator lotroGameMediator = runningTable.getLotroGameMediator();
                if (lotroGameMediator != null)
                    visitor.visitTable(nonPlayingGame.getKey(), lotroGameMediator.getGameId(), false, HallInfoVisitor.TableStatus.FINISHED, lotroGameMediator.getGameStatus(), runningTable.getFormatName(), runningTable.getTournamentName(), lotroGameMediator.getPlayersPlaying(), lotroGameMediator.getPlayersPlaying().contains(player.getName()), lotroGameMediator.getWinner());
            }

            for (Map.Entry<String, TournamentQueue> tournamentQueueEntry : _tournamentQueues.entrySet()) {
                String tournamentQueueKey = tournamentQueueEntry.getKey();
                TournamentQueue tournamentQueue = tournamentQueueEntry.getValue();
                visitor.visitTournamentQueue(tournamentQueueKey, tournamentQueue.getCost(), tournamentQueue.getCollectionType().getFullName(),
                        _formatLibrary.getFormat(tournamentQueue.getFormat()).getName(), tournamentQueue.getTournamentQueueName(),
                        tournamentQueue.getPrizesDescription(), tournamentQueue.getPairingDescription(), tournamentQueue.getStartCondition(),
                        tournamentQueue.getPlayerCount(), tournamentQueue.isPlayerSignedUp(player.getName()), tournamentQueue.isJoinable());
            }

            for (Map.Entry<String, Tournament> tournamentEntry : _runningTournaments.entrySet()) {
                String tournamentKey = tournamentEntry.getKey();
                Tournament tournament = tournamentEntry.getValue();
                visitor.visitTournament(tournamentKey, tournament.getCollectionType().getFullName(),
                        _formatLibrary.getFormat(tournament.getFormat()).getName(), tournament.getTournamentName(), tournament.getPlayOffSystem(),
                        tournament.getTournamentStage().getHumanReadable(),
                        tournament.getCurrentRound(), tournament.getPlayersInCompetitionCount(), tournament.isPlayerInCompetition(player.getName()));
            }
        } finally {
            _hallDataAccessLock.readLock().unlock();
        }
    }

    private LotroDeck validateUserAndDeck(LotroFormat format, Player player, String deckName, CollectionType collectionType, boolean competitive) throws HallException {
        LotroDeck lotroDeck = _lotroServer.getParticipantDeck(player, deckName);
        if (lotroDeck == null)
            throw new HallException("You don't have a deck registered yet");

        try {
            lotroDeck = validateUserAndDeck(format, player, collectionType, lotroDeck, competitive);
        } catch (DeckInvalidException e) {
            throw new HallException("Your selected deck is not valid for this format: " + e.getMessage());
        }

        return lotroDeck;
    }

    private LotroDeck validateUserAndDeck(LotroFormat format, Player player, CollectionType collectionType, LotroDeck lotroDeck, boolean competitive) throws HallException, DeckInvalidException {
        format.validateDeck(lotroDeck);

        // Now check if player owns all the cards
        if (collectionType.getCode().equals("default")) {
            CardCollection ownedCollection = _collectionsManager.getPlayerCollection(player, "permanent+trophy");

            LotroDeck filteredSpecialCardsDeck = new LotroDeck(lotroDeck.getDeckName());
            if (lotroDeck.getRing() != null)
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
                    String cardName = null;
                    try {
                        cardName = GameUtils.getFullName(_library.getLotroCardBlueprint(cardCount.getKey()));
                        throw new HallException("You don't have the required cards in collection: " + cardName + " required " + cardCount.getValue() + ", owned " + collectionCount);
                    } catch (CardNotFoundException e) {
                        // Ignore, card player has in a collection, should not disappear
                    }
                }
            }
        }
        return lotroDeck;
    }

    private String filterCard(String blueprintId, CardCollection ownedCollection) {
        if (ownedCollection.getItemCount(blueprintId) == 0)
            return _library.getBaseBlueprintId(blueprintId);
        return blueprintId;
    }

    private String getTournamentName(AwaitingTable table) {
        final League league = table.getGameSettings().getLeague();
        if (league != null)
            return league.getName() + " - " + table.getGameSettings().getLeagueSerie().getName();
        else
            return "Casual - " + table.getGameSettings().getTimerName();
    }

    private void createGameFromAwaitingTable(String tableId, AwaitingTable awaitingTable) {
        Set<LotroGameParticipant> players = awaitingTable.getPlayers();
        LotroGameParticipant[] participants = players.toArray(new LotroGameParticipant[players.size()]);
        final League league = awaitingTable.getGameSettings().getLeague();
        final LeagueSerieData leagueSerie = awaitingTable.getGameSettings().getLeagueSerie();

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

        createGame(tableId, participants, listener, getTournamentName(awaitingTable), awaitingTable.getGameSettings());
        _awaitingTables.remove(tableId);
    }

    private void createGame(String tableId, LotroGameParticipant[] participants, GameResultListener listener, String tournamentName, GameSettings gameSettings) {
        final LotroGameMediator lotroGameMediator = _lotroServer.createNewGame(tournamentName, participants, gameSettings);
        if (listener != null)
            lotroGameMediator.addGameResultListener(listener);
        lotroGameMediator.startGame();
        lotroGameMediator.addGameResultListener(_notifyHallListeners);
        _runningTables.put(tableId, new RunningTable(lotroGameMediator, gameSettings.getLotroFormat().getName(), tournamentName, gameSettings.getLeague(), gameSettings.getLeagueSerie()));
    }

    private class NotifyHallListenersGameResultListener implements GameResultListener {
        @Override
        public void gameCancelled() {
            hallChanged();
        }

        @Override
        public void gameFinished(String winnerPlayerId, String winReason, Map<String, String> loserPlayerIdsWithReasons) {
            hallChanged();
        }
    }

    private void joinTableInternal(String tableId, String player, AwaitingTable awaitingTable, LotroDeck lotroDeck) throws HallException {
        League league = awaitingTable.getGameSettings().getLeague();
        if (league != null) {
            LeagueSerieData leagueSerie = awaitingTable.getGameSettings().getLeagueSerie();
            if (!_leagueService.canPlayRankedGame(league, leagueSerie, player))
                throw new HallException("You have already played max games in league");
            if (awaitingTable.getPlayerNames().size() != 0 && !_leagueService.canPlayRankedGameAgainst(league, leagueSerie, awaitingTable.getPlayerNames().iterator().next(), player))
                throw new HallException("You have already played ranked league game against this player in that series");
        }
        boolean tableFull = awaitingTable.addPlayer(new LotroGameParticipant(player, lotroDeck));
        if (tableFull)
            createGameFromAwaitingTable(tableId, awaitingTable);
    }

    private int _tickCounter = 60;

    @Override
    protected void cleanup() {
        _hallDataAccessLock.writeLock().lock();
        try {
            // Remove finished games
            HashMap<String, RunningTable> copy = new HashMap<String, RunningTable>(_runningTables);
            for (Map.Entry<String, RunningTable> runningTable : copy.entrySet()) {
                LotroGameMediator lotroGameMediator = runningTable.getValue().getLotroGameMediator();
                if (lotroGameMediator.isDestroyed()) {
                    _runningTables.remove(runningTable.getKey());
                    hallChanged();
                }
            }

            long currentTime = System.currentTimeMillis();
            Map<Player, HallCommunicationChannel> visitCopy = new LinkedHashMap<Player, HallCommunicationChannel>(_playerChannelCommunication);
            for (Map.Entry<Player, HallCommunicationChannel> lastVisitedPlayer : visitCopy.entrySet()) {
                if (currentTime > lastVisitedPlayer.getValue().getLastAccessed() + _playerInactivityPeriod) {
                    Player player = lastVisitedPlayer.getKey();
                    _playerChannelCommunication.remove(player);
                    boolean leftTables = leaveAwaitingTablesForLeavingPlayer(player);
                    boolean leftQueues = leaveQueuesForLeavingPlayer(player);
                    if (leftTables || leftQueues)
                        hallChanged();
                }
            }

            for (Map.Entry<String, TournamentQueue> runningTournamentQueue : new HashMap<String, TournamentQueue>(_tournamentQueues).entrySet()) {
                String tournamentQueueKey = runningTournamentQueue.getKey();
                TournamentQueue tournamentQueue = runningTournamentQueue.getValue();
                HallTournamentQueueCallback queueCallback = new HallTournamentQueueCallback();
                // If it's finished, remove it
                if (tournamentQueue.process(queueCallback, _collectionsManager)) {
                    _tournamentQueues.remove(tournamentQueueKey);
                    hallChanged();
                }
            }

            for (Map.Entry<String, Tournament> tournamentEntry : new HashMap<String, Tournament>(_runningTournaments).entrySet()) {
                Tournament runningTournament = tournamentEntry.getValue();
                boolean changed = runningTournament.advanceTournament(new HallTournamentCallback(runningTournament), _collectionsManager);
                if (runningTournament.getTournamentStage() == Tournament.Stage.FINISHED)
                    _runningTournaments.remove(tournamentEntry.getKey());
                if (changed)
                    hallChanged();
            }

            if (_tickCounter == 60) {
                _tickCounter = 0;
                List<TournamentQueueInfo> unstartedTournamentQueues = _tournamentService.getUnstartedScheduledTournamentQueues(
                        System.currentTimeMillis() + _scheduledTournamentLoadTime);
                for (TournamentQueueInfo unstartedTournamentQueue : unstartedTournamentQueues) {
                    String scheduledTournamentId = unstartedTournamentQueue.getScheduledTournamentId();
                    if (!_tournamentQueues.containsKey(scheduledTournamentId)) {
                        ScheduledTournamentQueue scheduledQueue = new ScheduledTournamentQueue(scheduledTournamentId, unstartedTournamentQueue.getCost(),
                                true, _tournamentService, unstartedTournamentQueue.getStartTime(), unstartedTournamentQueue.getTournamentName(),
                                unstartedTournamentQueue.getFormat(), CollectionType.ALL_CARDS, Tournament.Stage.PLAYING_GAMES,
                                _pairingMechanismRegistry.getPairingMechanism(unstartedTournamentQueue.getPlayOffSystem()),
                                _tournamentPrizeSchemeRegistry.getTournamentPrizes(_cardSets, unstartedTournamentQueue.getPrizeScheme()), unstartedTournamentQueue.getMinimumPlayers());
                        _tournamentQueues.put(scheduledTournamentId, scheduledQueue);
                        hallChanged();
                    }
                }
            }
            _tickCounter++;

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
        private GameSettings tournamentGameSettings;

        private HallTournamentCallback(Tournament tournament) {
            _tournament = tournament;
            tournamentGameSettings = new GameSettings(null, _formatLibrary.getFormat(_tournament.getFormat()),
                    null, null, true, false, "Tournament", 60 * 40, 60 * 5);
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
                            }, _tournament.getTournamentName(), tournamentGameSettings);
                }
            } finally {
                _hallDataAccessLock.writeLock().unlock();
            }
        }

        @Override
        public void broadcastMessage(String message) {
            try {
                _hallChat.sendMessage("TournamentSystem", message, true);
            } catch (PrivateInformationException exp) {
                // Ignore, sent as admin
            } catch (ChatCommandErrorException e) {
                // Ignore, no command
            }
        }
    }
}
