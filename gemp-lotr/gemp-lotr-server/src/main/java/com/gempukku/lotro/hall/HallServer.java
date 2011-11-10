package com.gempukku.lotro.hall;

import com.gempukku.lotro.AbstractServer;
import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.db.CollectionDAO;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.Player;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.game.formats.*;
import com.gempukku.lotro.league.LeagueService;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HallServer extends AbstractServer {
    private ChatServer _chatServer;
    private LeagueService _leagueService;
    private CollectionDAO _collectionDao;
    private LotroServer _lotroServer;

    private Map<String, String> _supportedFormatNames = new LinkedHashMap<String, String>();
    private Map<String, LotroFormat> _supportedFormats = new HashMap<String, LotroFormat>();
    private Map<String, String> _formatCollectionIds = new HashMap<String, String>();

    // TODO Reading/writing from/to these maps is done in multiple threads
    private Map<String, AwaitingTable> _awaitingTables = new ConcurrentHashMap<String, AwaitingTable>();
    private Map<String, String> _runningTables = new ConcurrentHashMap<String, String>();
    private Map<String, String> _runningTableFormatNames = new ConcurrentHashMap<String, String>();
    private int _nextTableId = 1;

    private final int _playerInactivityPeriod = 1000 * 10; // 10 seconds

    private Map<String, Long> _lastVisitedPlayers = Collections.synchronizedMap(new LinkedHashMap<String, Long>());

    public HallServer(LotroServer lotroServer, ChatServer chatServer, LeagueService leagueService, CollectionDAO collectionDao, boolean test) {
        _lotroServer = lotroServer;
        _chatServer = chatServer;
        _leagueService = leagueService;
        _collectionDao = collectionDao;
        _chatServer.createChatRoom("Game Hall");

        addFormat("fotr_block", "Fellowship block", "default", new FotRBlockFormat(_lotroServer.getLotroCardBlueprintLibrary(), false));
        addFormat("c_fotr_block", "Community Fellowship block", "default", new FotRBlockFormat(_lotroServer.getLotroCardBlueprintLibrary(), true));
        addFormat("ttt_block", "Two Towers block", "default", new TTTBlockFormat(_lotroServer.getLotroCardBlueprintLibrary(), false));
        addFormat("c_ttt_block", "Community Two Towers block", "default", new TTTBlockFormat(_lotroServer.getLotroCardBlueprintLibrary(), true));
        addFormat("towers_standard", "Towers Standard", "default", new TowersStandardFormat(_lotroServer.getLotroCardBlueprintLibrary()));
        addFormat("king_block", "King block", "default", new KingBlockFormat(_lotroServer.getLotroCardBlueprintLibrary(), false));
        addFormat("c_king_block", "Community King block", "default", new KingBlockFormat(_lotroServer.getLotroCardBlueprintLibrary(), true));

        addFormat("whatever", "Format for testing", "default", new FreeFormat(_lotroServer.getLotroCardBlueprintLibrary()));
    }

    private void addFormat(String formatCode, String formatName, String formatCollectionId, LotroFormat format) {
        _supportedFormatNames.put(formatCode, formatName);
        _formatCollectionIds.put(formatCode, formatCollectionId);
        _supportedFormats.put(formatCode, format);
    }

    public int getTablesCount() {
        return _awaitingTables.size() + _runningTables.size();
    }

    public Map<String, String> getSupportedFormatNames() {
        return Collections.unmodifiableMap(_supportedFormatNames);
    }

    public LotroFormat getSupportedFormat(String formatId) {
        return _supportedFormats.get(formatId);
    }

    public Set<League> getRunningLeagues() {
        return _leagueService.getActiveLeagues();
    }

    /**
     * @param playerId
     * @return If table created, otherwise <code>false</code> (if the user already is sitting at a table or playing).
     */
    public synchronized void createNewTable(String type, String playerId, String deckName) throws HallException {
        LotroFormat supportedFormat = _supportedFormats.get(type);
        if (supportedFormat == null)
            throw new HallException("This format is not supported: " + type);

        LotroDeck lotroDeck = validateUserAndDeck(type, supportedFormat, playerId, deckName);

        String tableId = String.valueOf(_nextTableId++);
        AwaitingTable table = new AwaitingTable(type, _supportedFormatNames.get(type), supportedFormat);
        _awaitingTables.put(tableId, table);

        joinTableInternal(tableId, playerId, table, lotroDeck);
    }

    private LotroDeck validateUserAndDeck(String type, LotroFormat format, String playerId, String deckName) throws HallException {
        if (isPlayerBusy(playerId))
            throw new HallException("You can't play more than one game at a time or wait at more than one table");

        LotroDeck lotroDeck = _lotroServer.getParticipantDeck(playerId, deckName);
        if (lotroDeck == null)
            throw new HallException("You don't have a deck registered yet");

        try {
            format.validateDeck(lotroDeck);
        } catch (DeckInvalidException e) {
            throw new HallException("Your registered deck is not valid for this format: " + e.getMessage());
        }

        Player player = _lotroServer.getPlayerDao().getPlayer(playerId);
        CardCollection collection = _collectionDao.getCollectionForPlayer(player, _formatCollectionIds.get(type));
        // TODO check that player has cards in collection

        return lotroDeck;
    }

    /**
     * @param playerId
     * @return If table joined, otherwise <code>false</code> (if the user already is sitting at a table or playing).
     */
    public synchronized boolean joinTableAsPlayer(String tableId, String playerId, String deckName) throws HallException {
        AwaitingTable awaitingTable = _awaitingTables.get(tableId);
        if (awaitingTable == null)
            throw new HallException("Table is already taken or was removed");

        LotroDeck lotroDeck = validateUserAndDeck(awaitingTable.getFormatType(), awaitingTable.getLotroFormat(), playerId, deckName);

        joinTableInternal(tableId, playerId, awaitingTable, lotroDeck);

        return true;
    }

    private void joinTableInternal(String tableId, String playerId, AwaitingTable awaitingTable, LotroDeck lotroDeck) {
        boolean tableFull = awaitingTable.addPlayer(new LotroGameParticipant(playerId, lotroDeck));
        if (tableFull)
            createGame(tableId, awaitingTable);
    }

    private void createGame(String tableId, AwaitingTable awaitingTable) {
        Set<LotroGameParticipant> players = awaitingTable.getPlayers();
        LotroGameParticipant[] participants = players.toArray(new LotroGameParticipant[players.size()]);
        String gameId = _lotroServer.createNewGame(awaitingTable.getLotroFormat(), awaitingTable.getFormatName(), participants);
        LotroGameMediator lotroGameMediator = _lotroServer.getGameById(gameId);
        lotroGameMediator.startGame();
        _runningTables.put(tableId, gameId);
        _runningTableFormatNames.put(tableId, awaitingTable.getFormatName());
        _awaitingTables.remove(tableId);
    }

    public synchronized void leaveAwaitingTables(String playerId) {
        Map<String, AwaitingTable> copy = new HashMap<String, AwaitingTable>(_awaitingTables);
        for (Map.Entry<String, AwaitingTable> table : copy.entrySet()) {
            if (table.getValue().hasPlayer(playerId)) {
                boolean empty = table.getValue().removePlayer(playerId);
                if (empty)
                    _awaitingTables.remove(table.getKey());
            }
        }
    }

    private boolean isPlayerWaiting(String playerId) {
        for (AwaitingTable awaitingTable : _awaitingTables.values())
            if (awaitingTable.hasPlayer(playerId))
                return true;
        return false;
    }

    public void processTables(String participantId, HallInfoVisitor visitor) {
        _lastVisitedPlayers.put(participantId, System.currentTimeMillis());
        visitor.playerIsWaiting(isPlayerWaiting(participantId));

        Map<String, AwaitingTable> copy = new HashMap<String, AwaitingTable>(_awaitingTables);
        for (Map.Entry<String, AwaitingTable> table : copy.entrySet())
            visitor.visitTable(table.getKey(), null, "Waiting", table.getValue().getFormatName(), table.getValue().getPlayerNames(), null);

        Map<String, String> runningCopy = new LinkedHashMap<String, String>(_runningTables);
        for (Map.Entry<String, String> runningGame : runningCopy.entrySet()) {
            LotroGameMediator lotroGameMediator = _lotroServer.getGameById(runningGame.getValue());
            if (lotroGameMediator != null)
                visitor.visitTable(runningGame.getKey(), runningGame.getValue(), lotroGameMediator.getGameStatus(), _runningTableFormatNames.get(runningGame.getKey()), lotroGameMediator.getPlayersPlaying(), lotroGameMediator.getWinner());
        }

        String playerTable = getNonFinishedPlayerTable(participantId);
        if (playerTable != null) {
            String gameId = _runningTables.get(playerTable);
            if (gameId != null) {
                LotroGameMediator lotroGameMediator = _lotroServer.getGameById(gameId);
                if (lotroGameMediator != null && !lotroGameMediator.getGameStatus().equals("Finished"))
                    visitor.runningPlayerGame(gameId);
            }
        }
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
        // Remove finished games
        HashMap<String, String> copy = new HashMap<String, String>(_runningTables);
        for (Map.Entry<String, String> runningTable : copy.entrySet()) {
            if (_lotroServer.getGameById(runningTable.getValue()) == null) {
                _runningTables.remove(runningTable.getKey());
                _runningTableFormatNames.remove(runningTable.getKey());
            }
        }

        long currentTime = System.currentTimeMillis();
        Map<String, Long> visitCopy = new LinkedHashMap<String, Long>(_lastVisitedPlayers);
        for (Map.Entry<String, Long> lastVisitedPlayer : visitCopy.entrySet()) {
            if (currentTime > lastVisitedPlayer.getValue() + _playerInactivityPeriod) {
                String playerId = lastVisitedPlayer.getKey();
                _lastVisitedPlayers.remove(playerId);
                leaveAwaitingTables(playerId);
            }
        }
    }
}
