package com.gempukku.lotro.hall;

import com.gempukku.lotro.game.CardGameMediator;
import com.gempukku.lotro.game.GameParticipant;
import org.apache.log4j.Logger;

import java.util.*;

public class GameTable {
    private static final Logger logger = Logger.getLogger(GameTable.class);

    private final GameSettings gameSettings;
    private final Map<String, GameParticipant> players = new HashMap<>();

    private CardGameMediator cardGameMediator;
    private final int capacity;

    public GameTable(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
        this.capacity = gameSettings.getLotroFormat().getAdventure().isSolo() ? 1 : 2;
        logger.debug("Capacity of game: " + this.capacity);
    }

    public void startGame(CardGameMediator cardGameMediator) {
        logger.debug("GameTable - startGame function called;");
        this.cardGameMediator = cardGameMediator;
    }

    public CardGameMediator getLotroGameMediator() {
        return cardGameMediator;
    }

    public boolean wasGameStarted() {
        return cardGameMediator != null;
    }

    public boolean addPlayer(GameParticipant player) {
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

    public Set<GameParticipant> getPlayers() {
        return Collections.unmodifiableSet(new HashSet<>(players.values()));
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }
}
