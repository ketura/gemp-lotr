package com.gempukku.lotro.gamestate;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.PlayerOrder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//  Game stats to be delivered to the client

public class GameStats {
    private Map<String, Map<Zone, Integer>> _zoneSizes = new HashMap<>();
    private Map<String, Integer> _playerScores = new HashMap<>();

    /**
     * @return If the stats have changed
     */
    public boolean updateGameStats(DefaultGame game) {
        boolean changed = false;
        PlayerOrder playerOrder = game.getGameState().getPlayerOrder();

        Map<String, Map<Zone, Integer>> newZoneSizes = new HashMap<>();
        if (playerOrder != null) {
            for (String player : playerOrder.getAllPlayers()) {
                final HashMap<Zone, Integer> playerZoneSizes = new HashMap<>();
                playerZoneSizes.put(Zone.HAND, game.getGameState().getHand(player).size());
                playerZoneSizes.put(Zone.DECK, game.getGameState().getDeck(player).size());
                playerZoneSizes.put(Zone.ADVENTURE_DECK, game.getGameState().getAdventureDeck(player).size());
                playerZoneSizes.put(Zone.DISCARD, game.getGameState().getDiscard(player).size());
                playerZoneSizes.put(Zone.REMOVED, game.getGameState().getRemoved(player).size());
                newZoneSizes.put(player, playerZoneSizes);
            }
        }

        if (!newZoneSizes.equals(_zoneSizes)) {
            changed = true;
            _zoneSizes = newZoneSizes;
        }

        Map<String, Integer> newPlayerScores = new HashMap<>();
        if (playerOrder != null) {
            for (String player : playerOrder.getAllPlayers()) {
                newPlayerScores.put(player, game.getGameState().getPlayerScore(player));
            }
        }

        if (!newPlayerScores.equals(_playerScores)) {
            changed = true;
            _playerScores = newPlayerScores;
        }

        return changed;
    }

    public Map<String, Map<Zone, Integer>> getZoneSizes() {
        return Collections.unmodifiableMap(_zoneSizes);
    }

    public Map<String, Integer> getPlayerScores() { return Collections.unmodifiableMap(_playerScores); }

    public GameStats makeACopy() {
        GameStats copy = new GameStats();
        copy._zoneSizes = _zoneSizes;
        copy._playerScores = _playerScores;
        return copy;
    }
}
