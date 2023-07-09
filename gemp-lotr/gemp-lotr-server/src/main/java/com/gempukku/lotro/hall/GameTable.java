package com.gempukku.lotro.hall;

import com.gempukku.lotro.game.LotroGameMediator;
import com.gempukku.lotro.game.LotroGameParticipant;
import org.apache.log4j.Logger;

import java.util.*;

public class GameTable {
    private static final Logger logger = Logger.getLogger(GameTable.class);

    private final GameSettings gameSettings;
    private final Map<String, LotroGameParticipant> players = new HashMap<>();

    private LotroGameMediator lotroGameMediator;
    private final int capacity;

    public GameTable(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
        this.capacity = gameSettings.getLotroFormat().getAdventure().isSolo() ? 1 : 2;
        logger.debug("Capacity of game: " + this.capacity);
    }

    public void startGame(LotroGameMediator lotroGameMediator) {
        logger.debug("GameTable - startGame function called;");
        this.lotroGameMediator = lotroGameMediator;
    }

    public LotroGameMediator getLotroGameMediator() {
        return lotroGameMediator;
    }

    public boolean wasGameStarted() {
        return lotroGameMediator != null;
    }

    public boolean addPlayer(LotroGameParticipant player) {
        players.put(player.getPlayerId(), player);
        return players.size() == capacity;
    }

    public boolean removePlayer(String playerId) {
        players.remove(playerId);
        return players.size() == 0;
    }

    public boolean hasPlayer(String playerId) {
        return players.containsKey(playerId);
    }

    public List<String> getPlayerNames() {
        return new LinkedList<>(players.keySet());
    }

    public Set<LotroGameParticipant> getPlayers() {
        return Collections.unmodifiableSet(new HashSet<>(players.values()));
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }
}
