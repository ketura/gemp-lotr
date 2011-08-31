package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.timing.Action;

import java.util.*;

public class ModifiersLogic implements ModifiersEnvironment, ModifiersQuerying {
    private List<Modifier> _modifiers = new LinkedList<Modifier>();
    private Map<Phase, List<Modifier>> _untilStartOfPhaseModifiers = new HashMap<Phase, List<Modifier>>();
    private Map<Phase, List<Modifier>> _untilEndOfPhaseModifiers = new HashMap<Phase, List<Modifier>>();
    private List<Modifier> _untilEndOfTurnModifiers = new LinkedList<Modifier>();

    private Set<Modifier> _skipSet = new HashSet<Modifier>();

    private Map<Phase, Map<PhysicalCard, LimitCounter>> _counters = new HashMap<Phase, Map<PhysicalCard, LimitCounter>>();

    @Override
    public LimitCounter getUntilEndOfPhaseLimitCounter(PhysicalCard card, Phase phase) {
        Map<PhysicalCard, LimitCounter> limitCounterMap = _counters.get(phase);
        if (limitCounterMap == null) {
            limitCounterMap = new HashMap<PhysicalCard, LimitCounter>();
            _counters.put(phase, limitCounterMap);
        }
        LimitCounter limitCounter = limitCounterMap.get(card);
        if (limitCounter == null) {
            limitCounter = new DefaultLimitCounter();
            limitCounterMap.put(card, limitCounter);
        }
        return limitCounter;
    }

    @Override
    public ModifierHook addAlwaysOnModifier(Modifier modifier) {
        _modifiers.add(modifier);
        return new ModifierHookImpl(modifier);
    }

    public void removeEndOfPhase(Phase phase) {
        List<Modifier> list = _untilEndOfPhaseModifiers.get(phase);
        if (list != null) {
            _modifiers.removeAll(list);
            list.clear();
        }
        Map<PhysicalCard, LimitCounter> counterMap = _counters.get(phase);
        if (counterMap != null)
            counterMap.clear();
    }

    public void removeStartOfPhase(Phase phase) {
        List<Modifier> list = _untilStartOfPhaseModifiers.get(phase);
        if (list != null) {
            _modifiers.removeAll(list);
            list.clear();
        }
    }

    public void removeEndOfTurn() {
        _modifiers.removeAll(_untilEndOfTurnModifiers);
        _untilEndOfTurnModifiers.clear();
    }

    @Override
    public void addUntilEndOfPhaseModifier(Modifier modifier, Phase phase) {
        _modifiers.add(modifier);
        List<Modifier> list = _untilEndOfPhaseModifiers.get(phase);
        if (list == null) {
            list = new LinkedList<Modifier>();
            _untilEndOfPhaseModifiers.put(phase, list);
        }
        list.add(modifier);
    }

    @Override
    public void addUntilStartOfPhaseModifier(Modifier modifier, Phase phase) {
        _modifiers.add(modifier);
        List<Modifier> list = _untilStartOfPhaseModifiers.get(phase);
        if (list == null) {
            list = new LinkedList<Modifier>();
            _untilStartOfPhaseModifiers.put(phase, list);
        }
        list.add(modifier);
    }

    @Override
    public void addUntilEndOfTurnModifier(Modifier modifier) {
        _modifiers.add(modifier);
        _untilEndOfTurnModifiers.add(modifier);
    }

    @Override
    public List<Modifier> getModifiersAffecting(GameState gameState, PhysicalCard card) {
        List<Modifier> result = new LinkedList<Modifier>();
        for (Modifier modifier : _modifiers) {
            if (affectsCardWithSkipSet(gameState, card, modifier))
                result.add(modifier);
        }
        return result;
    }

    private boolean affectsCardWithSkipSet(GameState gameState, PhysicalCard physicalCard, Modifier modifier) {
        if (!_skipSet.contains(modifier)) {
            _skipSet.add(modifier);
            boolean result = modifier.affectsCard(gameState, this, physicalCard);
            _skipSet.remove(modifier);
            return result;
        } else {
            return false;
        }
    }

    @Override
    public boolean hasKeyword(GameState gameState, PhysicalCard physicalCard, Keyword keyword) {
        boolean result = physicalCard.getBlueprint().hasKeyword(keyword);
        for (Modifier modifier : _modifiers) {
            if (affectsCardWithSkipSet(gameState, physicalCard, modifier))
                result = modifier.hasKeyword(gameState, this, physicalCard, keyword, result);
        }
        return result;
    }

    @Override
    public int getKeywordCount(GameState gameState, PhysicalCard physicalCard, Keyword keyword) {
        int result = physicalCard.getBlueprint().getKeywordCount(keyword);
        for (Modifier modifier : _modifiers) {
            if (affectsCardWithSkipSet(gameState, physicalCard, modifier))
                result = modifier.getKeywordCount(gameState, this, physicalCard, keyword, result);
        }
        return result;
    }

    @Override
    public int getArcheryTotal(GameState gameState, Side side, int baseArcheryTotal) {
        int result = baseArcheryTotal;
        for (Modifier modifier : _modifiers)
            result = modifier.getArcheryTotal(gameState, this, side, result);
        return result;
    }

    @Override
    public int getMoveLimit(GameState gameState, int baseMoveLimit) {
        int result = baseMoveLimit;
        for (Modifier modifier : _modifiers)
            result = modifier.getMoveLimit(gameState, this, result);

        return result;
    }

    @Override
    public int getStrength(GameState gameState, PhysicalCard physicalCard) {
        int result = physicalCard.getBlueprint().getStrength();
        for (Modifier modifier : _modifiers) {
            if (affectsCardWithSkipSet(gameState, physicalCard, modifier)
                    && appliesStrengthModifier(gameState, modifier.getSource()))
                result = modifier.getStrength(gameState, this, physicalCard, result);
        }
        return result;
    }

    private boolean appliesStrengthModifier(GameState gameState, PhysicalCard modifierSource) {
        if (modifierSource == null)
            return true;
        boolean result = true;
        for (Modifier modifier : _modifiers) {
            if (affectsCardWithSkipSet(gameState, modifierSource, modifier))
                result = modifier.appliesStrengthModifier(gameState, this, modifierSource, result);
        }
        return result;
    }

    @Override
    public int getVitality(GameState gameState, PhysicalCard physicalCard) {
        int result = physicalCard.getBlueprint().getVitality();
        for (Modifier modifier : _modifiers) {
            if (affectsCardWithSkipSet(gameState, physicalCard, modifier))
                result = modifier.getVitality(gameState, this, physicalCard, result);
        }
        return result;
    }

    @Override
    public int getTwilightCost(GameState gameState, PhysicalCard physicalCard) {
        int result = physicalCard.getBlueprint().getTwilightCost();
        for (Modifier modifier : _modifiers) {
            if (affectsCardWithSkipSet(gameState, physicalCard, modifier))
                result = modifier.getTwilightCost(gameState, this, physicalCard, result);
        }
        return result;
    }

    @Override
    public int getPlayOnTwilightCost(GameState gameState, PhysicalCard physicalCard, PhysicalCard target) {
        int result = getTwilightCost(gameState, physicalCard);
        for (Modifier modifier : _modifiers) {
            if (affectsCardWithSkipSet(gameState, physicalCard, modifier))
                result = modifier.getPlayOnTwilightCost(gameState, this, physicalCard, target, result);
        }
        return result;
    }

    @Override
    public boolean isOverwhelmedByStrength(GameState gameState, PhysicalCard card, int strength, int opposingStrength) {
        boolean result = (opposingStrength > strength * 2);
        for (Modifier modifier : _modifiers) {
            if (affectsCardWithSkipSet(gameState, card, modifier))
                result = modifier.isOverwhelmedByStrength(gameState, this, card, strength, opposingStrength, result);
        }
        return result;
    }

    @Override
    public boolean canTakeWound(GameState gameState, PhysicalCard card) {
        boolean result = true;
        for (Modifier modifier : _modifiers) {
            if (affectsCardWithSkipSet(gameState, card, modifier))
                result = modifier.canTakeWound(gameState, this, card, result);
        }
        return result;
    }

    @Override
    public boolean isAllyOnCurrentSite(GameState gameState, PhysicalCard card) {
        boolean allyOnCurrentSite = (card.getBlueprint().getCardType() == CardType.ALLY
                && gameState.getCurrentSiteNumber() == card.getBlueprint().getSiteNumber());
        for (Modifier modifier : _modifiers) {
            if (affectsCardWithSkipSet(gameState, card, modifier))
                allyOnCurrentSite = modifier.isAllyOnCurrentSite(gameState, this, card, allyOnCurrentSite);
        }
        return allyOnCurrentSite;
    }

    @Override
    public boolean addsToArcheryTotal(GameState gameState, PhysicalCard card) {
        boolean result = true;
        for (Modifier modifier : _modifiers)
            if (affectsCardWithSkipSet(gameState, card, modifier))
                result = modifier.addsToArcheryTotal(gameState, this, card, result);

        return result;
    }

    @Override
    public boolean canPlayAction(GameState gameState, Action action) {
        boolean result = true;
        for (Modifier modifier : _modifiers)
            result = modifier.canPlayAction(gameState, this, action, result);
        return result;
    }

    @Override
    public boolean shouldSkipPhase(GameState gameState, Phase phase) {
        boolean result = false;
        for (Modifier modifier : _modifiers)
            result = modifier.shouldSkipPhase(gameState, this, phase, result);
        return result;
    }

    @Override
    public boolean isValidFreePlayerAssignments(GameState gameState, PhysicalCard companion, List<PhysicalCard> minions) {
        boolean result = true;
        for (Modifier modifier : _modifiers)
            if (affectsCardWithSkipSet(gameState, companion, modifier))
                result = modifier.isValidFreePlayerAssignments(gameState, this, companion, minions, result);
        return result;
    }

    private class ModifierHookImpl implements ModifierHook {
        private Modifier _modifier;

        private ModifierHookImpl(Modifier modifier) {
            _modifier = modifier;
        }

        @Override
        public void stop() {
            _modifiers.remove(_modifier);
        }
    }
}
