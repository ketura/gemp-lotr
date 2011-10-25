package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayerOrder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameStats {
    private int _fellowshipArchery;
    private int _shadowArchery;

    private int _moveLimit;
    private int _moveCount;

    private int _fellowshipSkirmishStrength;
    private int _shadowSkirmishStrength;

    private Map<String, Map<Zone, Integer>> _zoneSizes = new HashMap<String, Map<Zone, Integer>>();
    private Map<Integer, Integer> _charStrengths = new HashMap<Integer, Integer>();

    /**
     * @return If the stats have changed
     */
    public boolean updateGameStats(LotroGame game) {
        boolean changed = false;

        int newFellowshipArcheryTotal = RuleUtils.calculateFellowshipArcheryTotal(game);
        if (newFellowshipArcheryTotal != _fellowshipArchery) {
            changed = true;
            _fellowshipArchery = newFellowshipArcheryTotal;
        }

        int newShadowArcheryTotal = RuleUtils.calculateShadowArcheryTotal(game);
        if (newShadowArcheryTotal != _shadowArchery) {
            changed = true;
            _shadowArchery = newShadowArcheryTotal;
        }

        int newMoveLimit = RuleUtils.calculateMoveLimit(game);
        if (newMoveLimit != _moveLimit) {
            changed = true;
            _moveLimit = newMoveLimit;
        }

        int newMoveCount = game.getGameState().getMoveCount();
        if (newMoveCount != _moveCount) {
            changed = true;
            _moveCount = newMoveCount;
        }

        int newFellowshipStrength = RuleUtils.getFellowshipSkirmishStrength(game);
        if (newFellowshipStrength != _fellowshipSkirmishStrength) {
            changed = true;
            _fellowshipSkirmishStrength = newFellowshipStrength;
        }

        int newShadowStrength = RuleUtils.getShadowSkirmishStrength(game);
        if (newShadowStrength != _shadowSkirmishStrength) {
            changed = true;
            _shadowSkirmishStrength = newShadowStrength;
        }

        Map<String, Map<Zone, Integer>> newZoneSizes = new HashMap<String, Map<Zone, Integer>>();
        PlayerOrder playerOrder = game.getGameState().getPlayerOrder();
        if (playerOrder != null) {
            for (String player : playerOrder.getAllPlayers()) {
                final HashMap<Zone, Integer> playerZoneSizes = new HashMap<Zone, Integer>();
                playerZoneSizes.put(Zone.HAND, game.getGameState().getHand(player).size());
                playerZoneSizes.put(Zone.DECK, game.getGameState().getDeck(player).size());
                playerZoneSizes.put(Zone.DISCARD, game.getGameState().getDiscard(player).size());
                playerZoneSizes.put(Zone.DEAD, game.getGameState().getDeadPile(player).size());
                newZoneSizes.put(player, playerZoneSizes);
            }
        }

        if (!newZoneSizes.equals(_zoneSizes)) {
            changed = true;
            _zoneSizes = newZoneSizes;
        }

        Map<Integer, Integer> newCharStrengths = new HashMap<Integer, Integer>();
        for (PhysicalCard character : Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.or(CardType.COMPANION, CardType.ALLY, CardType.MINION)))
            newCharStrengths.put(character.getCardId(), game.getModifiersQuerying().getStrength(game.getGameState(), character));

        if (!newCharStrengths.equals(_charStrengths)) {
            changed = true;
            _charStrengths = newCharStrengths;
        }

        return changed;
    }

    public int getFellowshipArchery() {
        return _fellowshipArchery;
    }

    public int getShadowArchery() {
        return _shadowArchery;
    }

    public int getMoveLimit() {
        return _moveLimit;
    }

    public int getMoveCount() {
        return _moveCount;
    }

    public int getFellowshipSkirmishStrength() {
        return _fellowshipSkirmishStrength;
    }

    public int getShadowSkirmishStrength() {
        return _shadowSkirmishStrength;
    }

    public Map<String, Map<Zone, Integer>> getZoneSizes() {
        return Collections.unmodifiableMap(_zoneSizes);
    }

    public Map<Integer, Integer> getCharStrengths() {
        return Collections.unmodifiableMap(_charStrengths);
    }

    public GameStats makeACopy() {
        GameStats copy = new GameStats();
        copy._fellowshipArchery = _fellowshipArchery;
        copy._fellowshipSkirmishStrength = _fellowshipSkirmishStrength;
        copy._moveCount = _moveCount;
        copy._moveLimit = _moveLimit;
        copy._shadowArchery = _shadowArchery;
        copy._shadowSkirmishStrength = _shadowSkirmishStrength;
        copy._zoneSizes = _zoneSizes;
        copy._charStrengths = _charStrengths;
        return copy;
    }
}
