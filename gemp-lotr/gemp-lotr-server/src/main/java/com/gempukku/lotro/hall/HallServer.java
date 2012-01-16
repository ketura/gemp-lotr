package com.gempukku.lotro.hall;

import com.gempukku.lotro.AbstractServer;
import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueSerie;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.game.formats.*;
import com.gempukku.lotro.league.LeagueService;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HallServer extends AbstractServer {
    private ChatServer _chatServer;
    private LeagueService _leagueService;
    private LotroCardBlueprintLibrary _library;
    private CollectionsManager _collectionsManager;
    private LotroServer _lotroServer;

    private CollectionType _allCardsCollectionType = new CollectionType("default", "All cards");

    private final int _playerInactivityPeriod = 1000 * 20; // 10 seconds

    private int _nextTableId = 1;

    private String _motd;

    private boolean _shutdown;

    private Map<String, LotroFormat> _supportedFormats = new LinkedHashMap<String, LotroFormat>();


    private ReadWriteLock _hallDataAccessLock = new ReentrantReadWriteLock(false);

    private Map<String, AwaitingTable> _awaitingTables = new LinkedHashMap<String, AwaitingTable>();
    private Map<String, String> _runningTables = new LinkedHashMap<String, String>();

    private Map<String, String> _runningTableFormatNames = new HashMap<String, String>();
    private Map<String, String> _runningTableTournamentNames = new HashMap<String, String>();

    private Map<Player, Long> _lastVisitedPlayers = new HashMap<Player, Long>();

    public HallServer(LotroServer lotroServer, ChatServer chatServer, LeagueService leagueService, LotroCardBlueprintLibrary library, CollectionsManager collectionsManager, boolean test) {
        _lotroServer = lotroServer;
        _chatServer = chatServer;
        _leagueService = leagueService;
        _library = library;
        _collectionsManager = collectionsManager;
        _chatServer.createChatRoom("Game Hall", 10);

        addFormat("fotr_block", new FotRBlockFormat(library));
        addFormat("ttt_block", new TTTBlockFormat(library));
        addFormat("towers_standard", new TowersStandardFormat(library));
        addFormat("king_block", new KingBlockFormat(library));
        addFormat("movie", new MovieFormat(library));
        addFormat("war_block", new WarOfTheRingBlockFormat(library));
        addFormat("open", new OpenFormat(library));
        addFormat("expanded", new ExpandedFormat(library));
    }

    public void setShutdown(boolean shutdown) {
        _shutdown = shutdown;
        if (shutdown)
            cancelWaitingTables();
    }

    public String getMOTD() {
        return _motd;
    }

    public void setMOTD(String motd) {
        _motd = motd;
    }

    private void addFormat(String formatCode, LotroFormat format) {
        _supportedFormats.put(formatCode, format);
    }

    public int getTablesCount() {
        return _awaitingTables.size() + _runningTables.size();
    }

    public Map<String, LotroFormat> getSupportedFormats() {
        return Collections.unmodifiableMap(_supportedFormats);
    }

    private void cancelWaitingTables() {
        _hallDataAccessLock.writeLock().lock();
        try {
            _awaitingTables.clear();
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    /**
     * @return If table created, otherwise <code>false</code> (if the user already is sitting at a table or playing).
     */
    public void createNewTable(String type, Player player, String deckName) throws HallException {
        if (_shutdown)
            throw new HallException("Server is in shutdown mode. Server will be restarted after all running games are finished.");

        _hallDataAccessLock.writeLock().lock();
        try {
            League league = null;
            LeagueSerie leagueSerie = null;
            CollectionType collectionType = _allCardsCollectionType;
            LotroFormat format = _supportedFormats.get(type);

            if (format == null) {
                // Maybe it's a league format?
                league = _leagueService.getLeagueByType(type);
                if (league != null) {
                    leagueSerie = _leagueService.getCurrentLeagueSerie(league);
                    if (leagueSerie == null)
                        throw new HallException("There is no ongoing serie for that league");
                    format = _supportedFormats.get(leagueSerie.getFormat());
                    collectionType = league.getCollectionType();
                }
            }
            // It's not a normal format and also not a league one
            if (format == null)
                throw new HallException("This format is not supported: " + type);

            LotroDeck lotroDeck = validateUserAndDeck(format, player, deckName, collectionType);

            String tableId = String.valueOf(_nextTableId++);
            AwaitingTable table = new AwaitingTable(format, collectionType, league, leagueSerie);
            _awaitingTables.put(tableId, table);

            joinTableInternal(tableId, player.getName(), table, deckName, lotroDeck);
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }

    /**
     * @return If table joined, otherwise <code>false</code> (if the user already is sitting at a table or playing).
     */
    public boolean joinTableAsPlayer(String tableId, Player player, String deckName) throws HallException {
        _hallDataAccessLock.writeLock().lock();
        try {
            AwaitingTable awaitingTable = _awaitingTables.get(tableId);
            if (awaitingTable == null)
                throw new HallException("Table is already taken or was removed");

            LotroDeck lotroDeck = validateUserAndDeck(awaitingTable.getLotroFormat(), player, deckName, awaitingTable.getCollectionType());

            joinTableInternal(tableId, player.getName(), awaitingTable, deckName, lotroDeck);

            return true;
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

    public void processTables(Player player, HallInfoVisitor visitor) {
        _hallDataAccessLock.readLock().lock();
        try {
            _lastVisitedPlayers.put(player, System.currentTimeMillis());
            visitor.playerIsWaiting(isPlayerWaiting(player.getName()));

            // First waiting
            for (Map.Entry<String, AwaitingTable> tableInformation : _awaitingTables.entrySet()) {
                final AwaitingTable table = tableInformation.getValue();

                visitor.visitTable(tableInformation.getKey(), null, "Waiting", table.getLotroFormat().getName(), getTournamentName(table), table.getPlayerNames(), null);
            }

            // Then non-finished
            Map<String, String> nonPlayingTables = new HashMap<String, String>();

            for (Map.Entry<String, String> runningGame : _runningTables.entrySet()) {
                LotroGameMediator lotroGameMediator = _lotroServer.getGameById(runningGame.getValue());
                if (lotroGameMediator != null) {
                    String gameStatus = lotroGameMediator.getGameStatus();
                    if (!gameStatus.equals("Finished"))
                        visitor.visitTable(runningGame.getKey(), runningGame.getValue(), gameStatus, _runningTableFormatNames.get(runningGame.getKey()), _runningTableTournamentNames.get(runningGame.getKey()), lotroGameMediator.getPlayersPlaying(), lotroGameMediator.getWinner());
                    else
                        nonPlayingTables.put(runningGame.getKey(), runningGame.getValue());
                }
            }

            // Then rest
            for (Map.Entry<String, String> nonPlayingGame : nonPlayingTables.entrySet()) {
                LotroGameMediator lotroGameMediator = _lotroServer.getGameById(nonPlayingGame.getValue());
                if (lotroGameMediator != null)
                    visitor.visitTable(nonPlayingGame.getKey(), nonPlayingGame.getValue(), lotroGameMediator.getGameStatus(), _runningTableFormatNames.get(nonPlayingGame.getKey()), _runningTableTournamentNames.get(nonPlayingGame.getKey()), lotroGameMediator.getPlayersPlaying(), lotroGameMediator.getWinner());
            }

            String playerTable = getNonFinishedPlayerTable(player.getName());
            if (playerTable != null) {
                String gameId = _runningTables.get(playerTable);
                if (gameId != null) {
                    LotroGameMediator lotroGameMediator = _lotroServer.getGameById(gameId);
                    if (lotroGameMediator != null && !lotroGameMediator.getGameStatus().equals("Finished"))
                        visitor.runningPlayerGame(gameId);
                }
            }
        } finally {
            _hallDataAccessLock.readLock().unlock();
        }
    }

    private LotroDeck validateUserAndDeck(LotroFormat format, Player player, String deckName, CollectionType collectionType) throws HallException {
        if (isPlayerBusy(player.getName()))
            throw new HallException("You can't play more than one game at a time or wait at more than one table");

        LotroDeck lotroDeck = _lotroServer.getParticipantDeck(player, deckName);
        if (lotroDeck == null)
            throw new HallException("You don't have a deck registered yet");

        try {
            format.validateDeck(player, lotroDeck);
        } catch (DeckInvalidException e) {
            throw new HallException("Your selected deck is not valid for this format: " + e.getMessage());
        }

        // Now check if player owns all the cards
        if (collectionType.getCode().equals("default")) {
            CardCollection ownedCollection = _collectionsManager.getPlayerCollection(player, "permanent");

            LotroDeck filteredSpecialCardsDeck = new LotroDeck();
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

                for (int i = 0; i < owned; i++)
                    filteredSpecialCardsDeck.addCard(blueprintId);
                for (int i = 0; i < (count - owned); i++)
                    filteredSpecialCardsDeck.addCard(_library.getBaseBlueprintId(blueprintId));
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
                    String cardName = _library.getLotroCardBlueprint(cardCount.getKey()).getName();
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
            tournamentName = league.getName() + " - " + table.getLeagueSerie().getType();
        return tournamentName;
    }

    private void createGame(String tableId, AwaitingTable awaitingTable) {
        Set<LotroGameParticipant> players = awaitingTable.getPlayers();
        LotroGameParticipant[] participants = players.toArray(new LotroGameParticipant[players.size()]);
        League league = awaitingTable.getLeague();
        String gameId = _lotroServer.createNewGame(awaitingTable.getLotroFormat(), participants, league != null);
        LotroGameMediator lotroGameMediator = _lotroServer.getGameById(gameId);
        if (league != null)
            _leagueService.leagueGameStarting(league, awaitingTable.getLeagueSerie(), lotroGameMediator);
        lotroGameMediator.startGame();
        _runningTables.put(tableId, gameId);
        _runningTableFormatNames.put(tableId, awaitingTable.getLotroFormat().getName());
        _runningTableTournamentNames.put(tableId, getTournamentName(awaitingTable));
        _awaitingTables.remove(tableId);
    }

    private void joinTableInternal(String tableId, String playerId, AwaitingTable awaitingTable, String deckName, LotroDeck lotroDeck) {
        boolean tableFull = awaitingTable.addPlayer(new LotroGameParticipant(playerId, deckName, lotroDeck));
        if (tableFull)
            createGame(tableId, awaitingTable);
    }

    private boolean isPlayerWaiting(String playerId) {
        for (AwaitingTable awaitingTable : _awaitingTables.values())
            if (awaitingTable.hasPlayer(playerId))
                return true;
        return false;
    }

    private String getNonFinishedPlayerTable(String playerId) {
        for (Map.Entry<String, AwaitingTable> table : _awaitingTables.entrySet()) {
            if (table.getValue().hasPlayer(playerId))
                return table.getKey();
        }

        for (Map.Entry<String, String> runningTable : _runningTables.entrySet()) {
            String gameId = runningTable.getValue();
            LotroGameMediator lotroGameMediator = _lotroServer.getGameById(gameId);
            if (lotroGameMediator != null && !lotroGameMediator.getGameStatus().equals("Finished"))
                if (lotroGameMediator.getPlayersPlaying().contains(playerId))
                    return runningTable.getKey();
        }

        return null;
    }

    private boolean isPlayerBusy(String playerId) {
        for (AwaitingTable awaitingTable : _awaitingTables.values())
            if (awaitingTable.hasPlayer(playerId))
                return true;

        for (String gameId : _runningTables.values()) {
            LotroGameMediator lotroGameMediator = _lotroServer.getGameById(gameId);
            if (lotroGameMediator != null && !lotroGameMediator.getGameStatus().equals("Finished") && lotroGameMediator.getPlayersPlaying().contains(playerId))
                return true;
        }
        return false;
    }

    @Override
    protected void cleanup() {
        _hallDataAccessLock.writeLock().lock();
        try {
            // Remove finished games
            HashMap<String, String> copy = new HashMap<String, String>(_runningTables);
            for (Map.Entry<String, String> runningTable : copy.entrySet()) {
                if (_lotroServer.getGameById(runningTable.getValue()) == null) {
                    _runningTables.remove(runningTable.getKey());
                    _runningTableFormatNames.remove(runningTable.getKey());
                    _runningTableTournamentNames.remove(runningTable.getKey());
                }
            }

            long currentTime = System.currentTimeMillis();
            Map<Player, Long> visitCopy = new LinkedHashMap<Player, Long>(_lastVisitedPlayers);
            for (Map.Entry<Player, Long> lastVisitedPlayer : visitCopy.entrySet()) {
                if (currentTime > lastVisitedPlayer.getValue() + _playerInactivityPeriod) {
                    Player player = lastVisitedPlayer.getKey();
                    _lastVisitedPlayers.remove(player);
                    leaveAwaitingTables(player);
                }
            }
        } finally {
            _hallDataAccessLock.writeLock().unlock();
        }
    }
}
