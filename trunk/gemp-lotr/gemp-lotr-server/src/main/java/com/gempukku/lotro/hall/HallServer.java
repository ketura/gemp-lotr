package com.gempukku.lotro.hall;

import com.gempukku.lotro.AbstractServer;
import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.DeckInvalidException;
import com.gempukku.lotro.game.LotroGameMediator;
import com.gempukku.lotro.game.LotroGameParticipant;
import com.gempukku.lotro.game.LotroServer;
import com.gempukku.lotro.game.formats.FotRBlockFormat;
import com.gempukku.lotro.game.formats.LotroFormat;
import com.gempukku.lotro.game.formats.ModifiedFotRBlockFormat;
import com.gempukku.lotro.league.LeagueService;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HallServer extends AbstractServer {
    private ChatServer _chatServer;
    private LeagueService _leagueService;
    private LotroServer _lotroServer;

    private Map<String, LotroFormat> _supportedFormats = new HashMap<String, LotroFormat>();

    private Map<String, AwaitingTable> _awaitingTables = new ConcurrentHashMap<String, AwaitingTable>();
    private Map<String, String> _runningTables = new ConcurrentHashMap<String, String>();
    private int _nextTableId = 1;

    private final int _playerInactivityPeriod = 1000 * 10; // 10 seconds

    private Map<String, Long> _lastVisitedPlayers = Collections.synchronizedMap(new LinkedHashMap<String, Long>());

    public HallServer(LotroServer lotroServer, ChatServer chatServer, LeagueService leagueService, boolean test) {
        _lotroServer = lotroServer;
        _chatServer = chatServer;
        _leagueService = leagueService;
        _chatServer.createChatRoom("Game Hall");

        _supportedFormats.put("FotR block", new FotRBlockFormat(_lotroServer.getLotroCardBlueprintLibrary()));
        if (test)
            _supportedFormats.put("Modified FotR block", new ModifiedFotRBlockFormat(_lotroServer.getLotroCardBlueprintLibrary()));
    }

    public int getTablesCount() {
        return _awaitingTables.size() + _runningTables.size();
    }

    public Map<String, LotroFormat> getSupportedFormats() {
        return new TreeMap<String, LotroFormat>(_supportedFormats);
    }

    public Set<League> getRunningLeagues() {
        return _leagueService.getAllLeagues();
    }

    /**
     * @param playerId
     * @return If table created, otherwise <code>false</code> (if the user already is sitting at a table or playing).
     */
    public synchronized void createNewTable(String format, String playerId) throws HallException {
        LotroFormat supportedFormat = _supportedFormats.get(format);
        if (supportedFormat == null)
            throw new HallException("This format is not supported: " + format);

        LotroDeck lotroDeck = validateUserAndDeck(supportedFormat, playerId);

        String tableId = String.valueOf(_nextTableId++);
        AwaitingTable table = new AwaitingTable(format, supportedFormat);
        _awaitingTables.put(tableId, table);

        joinTableInternal(tableId, playerId, table, lotroDeck);
    }

    private LotroDeck validateUserAndDeck(LotroFormat format, String playerId) throws HallException {
        if (isPlayerBusy(playerId))
            throw new HallException("You can't play more than one game at a time or wait at more than one table");

        LotroDeck lotroDeck = _lotroServer.getParticipantDeck(playerId);
        if (lotroDeck == null)
            throw new HallException("You don't have a deck registered yet");

        try {
            format.validateDeck(lotroDeck);
        } catch (DeckInvalidException e) {
            throw new HallException("Your registered deck is not valid for this format: " + e.getMessage());
        }
        return lotroDeck;
    }

    /**
     * @param playerId
     * @return If table joined, otherwise <code>false</code> (if the user already is sitting at a table or playing).
     */
    public synchronized boolean joinTableAsPlayer(String tableId, String playerId) throws HallException {
        AwaitingTable awaitingTable = _awaitingTables.get(tableId);
        if (awaitingTable == null)
            throw new HallException("Table is already taken or was removed");

        LotroDeck lotroDeck = validateUserAndDeck(awaitingTable.getLotroFormat(), playerId);

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
        String gameId = _lotroServer.createNewGame(awaitingTable.getLotroFormat(), participants);
        LotroGameMediator lotroGameMediator = _lotroServer.getGameById(gameId);
        lotroGameMediator.startGame();
        _runningTables.put(tableId, gameId);
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
            visitor.visitTable(table.getKey(), null, "Waiting", table.getValue().getPlayerNames());

        Map<String, String> runningCopy = new LinkedHashMap<String, String>(_runningTables);
        for (Map.Entry<String, String> runningGame : runningCopy.entrySet()) {
            LotroGameMediator lotroGameMediator = _lotroServer.getGameById(runningGame.getValue());
            if (lotroGameMediator != null)
                visitor.visitTable(runningGame.getKey(), runningGame.getValue(), lotroGameMediator.getGameStatus(), lotroGameMediator.getPlayersPlaying());
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
            if (_lotroServer.getGameById(runningTable.getValue()) == null)
                _runningTables.remove(runningTable.getKey());
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
