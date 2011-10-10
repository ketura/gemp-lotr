package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.timing.Action;

import java.util.*;

public class ModifiersLogic implements ModifiersEnvironment, ModifiersQuerying {
    private Map<ModifierEffect, List<Modifier>> _modifiers = new HashMap<ModifierEffect, List<Modifier>>();

    private Map<Phase, List<Modifier>> _untilStartOfPhaseModifiers = new HashMap<Phase, List<Modifier>>();
    private Map<Phase, List<Modifier>> _untilEndOfPhaseModifiers = new HashMap<Phase, List<Modifier>>();
    private List<Modifier> _untilEndOfTurnModifiers = new LinkedList<Modifier>();

    private Set<Modifier> _skipSet = new HashSet<Modifier>();

    private Map<Phase, Map<PhysicalCard, LimitCounter>> _counters = new HashMap<Phase, Map<PhysicalCard, LimitCounter>>();

    private int _drawnThisPhaseCount = 0;

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

    private List<Modifier> getEffectModifiers(ModifierEffect modifierEffect) {
        List<Modifier> modifiers = _modifiers.get(modifierEffect);
        if (modifiers == null) {
            modifiers = new LinkedList<Modifier>();
            _modifiers.put(modifierEffect, modifiers);
        }
        return modifiers;
    }

    private void removeModifiers(List<Modifier> modifiers) {
        for (List<Modifier> list : _modifiers.values())
            list.removeAll(modifiers);
    }

    private void removeModifier(Modifier modifier) {
        for (List<Modifier> list : _modifiers.values())
            list.remove(modifier);
    }

    @Override
    public ModifierHook addAlwaysOnModifier(Modifier modifier) {
        addModifier(modifier);
        return new ModifierHookImpl(modifier);
    }

    private void addModifier(Modifier modifier) {
        ModifierEffect[] modifierEffects = modifier.getModifierEffects();
        for (ModifierEffect modifierEffect : modifierEffects)
            getEffectModifiers(modifierEffect).add(modifier);
    }

    private List<Modifier> getModifiers(ModifierEffect modifierEffect) {
        List<Modifier> modifiers = _modifiers.get(modifierEffect);
        if (modifiers == null)
            return Collections.emptyList();
        else
            return modifiers;
    }

    public void removeEndOfPhase(Phase phase) {
        List<Modifier> list = _untilEndOfPhaseModifiers.get(phase);
        if (list != null) {
            removeModifiers(list);
            list.clear();
        }
        Map<PhysicalCard, LimitCounter> counterMap = _counters.get(phase);
        if (counterMap != null)
            counterMap.clear();

        _drawnThisPhaseCount = 0;
    }

    public void removeStartOfPhase(Phase phase) {
        List<Modifier> list = _untilStartOfPhaseModifiers.get(phase);
        if (list != null) {
            removeModifiers(list);
            list.clear();
        }
    }

    public void removeEndOfTurn() {
        removeModifiers(_untilEndOfTurnModifiers);
        _untilEndOfTurnModifiers.clear();
    }

    @Override
    public void addUntilEndOfPhaseModifier(Modifier modifier, Phase phase) {
        addModifier(modifier);
        List<Modifier> list = _untilEndOfPhaseModifiers.get(phase);
        if (list == null) {
            list = new LinkedList<Modifier>();
            _untilEndOfPhaseModifiers.put(phase, list);
        }
        list.add(modifier);
    }

    @Override
    public void addUntilStartOfPhaseModifier(Modifier modifier, Phase phase) {
        addModifier(modifier);
        List<Modifier> list = _untilStartOfPhaseModifiers.get(phase);
        if (list == null) {
            list = new LinkedList<Modifier>();
            _untilStartOfPhaseModifiers.put(phase, list);
        }
        list.add(modifier);
    }

    @Override
    public void addUntilEndOfTurnModifier(Modifier modifier) {
        addModifier(modifier);
        _untilEndOfTurnModifiers.add(modifier);
    }

    @Override
    public Collection<Modifier> getModifiersAffecting(GameState gameState, PhysicalCard card) {
        Set<Modifier> result = new HashSet<Modifier>();
        for (List<Modifier> modifiers : _modifiers.values()) {
            for (Modifier modifier : modifiers) {
                if (affectsCardWithSkipSet(gameState, card, modifier))
                    result.add(modifier);
            }
        }
        return result;
    }

    private boolean affectsCardWithSkipSet(GameState gameState, PhysicalCard physicalCard, Modifier modifier) {
        if (!_skipSet.contains(modifier) && physicalCard != null) {
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
        for (Modifier modifier : getModifiers(ModifierEffect.KEYWORD_MODIFIER)) {
            if (affectsCardWithSkipSet(gameState, physicalCard, modifier))
                if (modifier.isKeywordRemoved(gameState, this, physicalCard, keyword))
                    return false;
        }

        boolean result = physicalCard.getBlueprint().hasKeyword(keyword);

        for (Modifier modifier : getModifiers(ModifierEffect.KEYWORD_MODIFIER)) {
            if (affectsCardWithSkipSet(gameState, physicalCard, modifier))
                result = modifier.hasKeyword(gameState, this, physicalCard, keyword, result);
        }
        return result;
    }

    @Override
    public int getKeywordCount(GameState gameState, PhysicalCard physicalCard, Keyword keyword) {
        for (Modifier modifier : getModifiers(ModifierEffect.KEYWORD_MODIFIER)) {
            if (affectsCardWithSkipSet(gameState, physicalCard, modifier))
                if (modifier.isKeywordRemoved(gameState, this, physicalCard, keyword))
                    return 0;
        }

        int result = physicalCard.getBlueprint().getKeywordCount(keyword);
        for (Modifier modifier : getModifiers(ModifierEffect.KEYWORD_MODIFIER)) {
            if (affectsCardWithSkipSet(gameState, physicalCard, modifier)
                    && appliesKeywordModifier(gameState, modifier.getSource(), keyword))
                result = modifier.getKeywordCount(gameState, this, physicalCard, keyword, result);
        }
        return Math.max(0, result);
    }

    private boolean appliesKeywordModifier(GameState gameState, PhysicalCard modifierSource, Keyword keyword) {
        if (modifierSource == null)
            return true;
        boolean result = true;
        for (Modifier modifier : getModifiers(ModifierEffect.KEYWORD_MODIFIER)) {
            if (affectsCardWithSkipSet(gameState, modifierSource, modifier))
                result = modifier.appliesKeywordModifier(gameState, this, modifierSource, keyword, result);
        }
        return result;
    }

    @Override
    public int getArcheryTotal(GameState gameState, Side side, int baseArcheryTotal) {
        int result = baseArcheryTotal;
        for (Modifier modifier : getModifiers(ModifierEffect.ARCHERY_MODIFIER))
            result = modifier.getArcheryTotal(gameState, this, side, result);
        return Math.max(0, result);
    }

    @Override
    public int getMoveLimit(GameState gameState, int baseMoveLimit) {
        int result = baseMoveLimit;
        for (Modifier modifier : getModifiers(ModifierEffect.MOVE_LIMIT_MODIFIER))
            result = modifier.getMoveLimit(gameState, this, result);
        return Math.max(1, result);
    }

    @Override
    public int getStrength(GameState gameState, PhysicalCard physicalCard) {
        int result = physicalCard.getBlueprint().getStrength();
        for (Modifier modifier : getModifiers(ModifierEffect.STRENGTH_MODIFIER)) {
            if (affectsCardWithSkipSet(gameState, physicalCard, modifier)
                    && appliesStrengthModifier(gameState, modifier.getSource()))
                result = modifier.getStrength(gameState, this, physicalCard, result);
        }
        return Math.max(0, result);
    }

    private boolean appliesStrengthModifier(GameState gameState, PhysicalCard modifierSource) {
        if (modifierSource == null)
            return true;
        boolean result = true;
        for (Modifier modifier : getModifiers(ModifierEffect.STRENGTH_MODIFIER)) {
            if (affectsCardWithSkipSet(gameState, modifierSource, modifier))
                result = modifier.appliesStrengthModifier(gameState, this, modifierSource, result);
        }
        return result;
    }

    @Override
    public int getVitality(GameState gameState, PhysicalCard physicalCard) {
        int result = physicalCard.getBlueprint().getVitality() - gameState.getWounds(physicalCard);
        for (Modifier modifier : getModifiers(ModifierEffect.VITALITY_MODIFIER)) {
            if (affectsCardWithSkipSet(gameState, physicalCard, modifier))
                result = modifier.getVitality(gameState, this, physicalCard, result);
        }
        return Math.max(0, result);
    }

    @Override
    public int getResistance(GameState gameState, PhysicalCard physicalCard) {
        int result = physicalCard.getBlueprint().getResistance() - gameState.getBurdens();
        for (Modifier modifier : getModifiers(ModifierEffect.RESISTANCE_MODIFIER)) {
            if (affectsCardWithSkipSet(gameState, physicalCard, modifier))
                result = modifier.getResistance(gameState, this, physicalCard, result);
        }
        return Math.max(0, result);
    }

    @Override
    public int getMinionSiteNumber(GameState gameState, PhysicalCard physicalCard) {
        int result = physicalCard.getBlueprint().getSiteNumber();
        for (Modifier modifier : getModifiers(ModifierEffect.SITE_NUMBER_MODIFIER)) {
            if (affectsCardWithSkipSet(gameState, physicalCard, modifier))
                result = modifier.getMinionSiteNumber(gameState, this, physicalCard, result);
        }
        return Math.max(0, result);
    }

    @Override
    public int getTwilightCost(GameState gameState, PhysicalCard physicalCard) {
        int result = physicalCard.getBlueprint().getTwilightCost();
        for (Modifier modifier : getModifiers(ModifierEffect.TWILIGHT_COST_MODIFIER)) {
            if (affectsCardWithSkipSet(gameState, physicalCard, modifier))
                result = modifier.getTwilightCost(gameState, this, physicalCard, result);
        }
        return Math.max(0, result);
    }

    @Override
    public int getPlayOnTwilightCost(GameState gameState, PhysicalCard physicalCard, PhysicalCard target) {
        int result = getTwilightCost(gameState, physicalCard);
        for (Modifier modifier : getModifiers(ModifierEffect.TWILIGHT_COST_MODIFIER)) {
            if (affectsCardWithSkipSet(gameState, physicalCard, modifier))
                result = modifier.getPlayOnTwilightCost(gameState, this, physicalCard, target, result);
        }
        return Math.max(0, result);
    }

    @Override
    public int getRoamingPenalty(GameState gameState, PhysicalCard physicalCard) {
        int result = 2;
        for (Modifier modifier : getModifiers(ModifierEffect.TWILIGHT_COST_MODIFIER)) {
            if (affectsCardWithSkipSet(gameState, physicalCard, modifier))
                result = modifier.getRoamingPenalty(gameState, this, physicalCard, result);
        }
        return Math.max(0, result);
    }

    @Override
    public boolean isOverwhelmedByStrength(GameState gameState, PhysicalCard card, int strength, int opposingStrength) {
        boolean result = (opposingStrength >= strength * 2);
        for (Modifier modifier : getModifiers(ModifierEffect.OVERWHELM_MODIFIER)) {
            if (affectsCardWithSkipSet(gameState, card, modifier))
                result = modifier.isOverwhelmedByStrength(gameState, this, card, strength, opposingStrength, result);
        }
        return result;
    }

    @Override
    public boolean canTakeWound(GameState gameState, PhysicalCard card) {
        boolean result = true;
        for (Modifier modifier : getModifiers(ModifierEffect.WOUND_MODIFIER)) {
            if (affectsCardWithSkipSet(gameState, card, modifier))
                result = modifier.canTakeWound(gameState, this, card, result);
        }
        return result;
    }

    @Override
    public boolean canBeExerted(GameState gameState, PhysicalCard source, PhysicalCard card) {
        boolean result = true;
        for (Modifier modifier : getModifiers(ModifierEffect.WOUND_MODIFIER)) {
            if (affectsCardWithSkipSet(gameState, card, modifier))
                result = modifier.canBeExerted(gameState, this, source, card, result);
        }
        return result;
    }

    @Override
    public boolean isAllyParticipateInArcheryFire(GameState gameState, PhysicalCard card) {
        for (Modifier modifier : getModifiers(ModifierEffect.PRESENCE_MODIFIER)) {
            if (affectsCardWithSkipSet(gameState, card, modifier))
                if (modifier.isAllyParticipateInArcheryFire(gameState, this, card))
                    return true;
        }
        return false;
    }

    @Override
    public boolean isAllyParticipateInSkirmishes(GameState gameState, Side sidePlayer, PhysicalCard card) {
        for (Modifier modifier : getModifiers(ModifierEffect.PRESENCE_MODIFIER)) {
            if (affectsCardWithSkipSet(gameState, card, modifier))
                if (modifier.isAllyParticipateInSkirmishes(gameState, sidePlayer, this, card))
                    return true;
        }
        return false;
    }

    @Override
    public boolean addsToArcheryTotal(GameState gameState, PhysicalCard card) {
        boolean result = true;
        for (Modifier modifier : getModifiers(ModifierEffect.ARCHERY_MODIFIER))
            if (affectsCardWithSkipSet(gameState, card, modifier))
                result = modifier.addsToArcheryTotal(gameState, this, card, result);

        return result;
    }

    @Override
    public boolean canPlayAction(GameState gameState, String performingPlayer, Action action) {
        boolean result = true;
        for (Modifier modifier : getModifiers(ModifierEffect.ACTION_MODIFIER))
            result = modifier.canPlayAction(gameState, this, performingPlayer, action, result);
        return result;
    }

    @Override
    public boolean canHavePlayedOn(GameState gameState, PhysicalCard playedCard, PhysicalCard target) {
        for (Modifier modifier : getModifiers(ModifierEffect.TARGET_MODIFIER))
            if (affectsCardWithSkipSet(gameState, target, modifier))
                if (!modifier.canHavePlayedOn(gameState, this, playedCard, target))
                    return false;
        return true;
    }

    @Override
    public boolean canHaveTransferredOn(GameState gameState, PhysicalCard playedCard, PhysicalCard target) {
        for (Modifier modifier : getModifiers(ModifierEffect.TARGET_MODIFIER))
            if (affectsCardWithSkipSet(gameState, target, modifier))
                if (!modifier.canHaveTransferredOn(gameState, this, playedCard, target))
                    return false;
        return true;
    }

    @Override
    public boolean shouldSkipPhase(GameState gameState, Phase phase, String playerId) {
        boolean result = false;
        for (Modifier modifier : getModifiers(ModifierEffect.ACTION_MODIFIER))
            result = modifier.shouldSkipPhase(gameState, this, phase, playerId, result);
        return result;
    }

    @Override
    public boolean isValidAssignments(GameState gameState, Side side, Map<PhysicalCard, List<PhysicalCard>> assignments) {
        boolean result = true;

        for (Modifier modifier : getModifiers(ModifierEffect.ASSIGNMENT_MODIFIER)) {
            result = modifier.isValidAssignments(gameState, side, this, assignments, result);
            for (Map.Entry<PhysicalCard, List<PhysicalCard>> assignment : assignments.entrySet()) {
                if (affectsCardWithSkipSet(gameState, assignment.getKey(), modifier))
                    result = modifier.isValidAssignments(gameState, side, this, assignment.getKey(), assignment.getValue(), result);
            }
        }
        return result;
    }

    @Override
    public boolean canBeAssignedToSkirmish(GameState gameState, Side sidePlayer, PhysicalCard card) {
        for (Modifier modifier : getModifiers(ModifierEffect.ASSIGNMENT_MODIFIER))
            if (affectsCardWithSkipSet(gameState, card, modifier))
                if (!modifier.canBeAssignedToSkirmish(gameState, sidePlayer, this, card))
                    return false;
        return true;
    }

    @Override
    public boolean canBeDiscardedFromPlay(GameState gameState, PhysicalCard card, PhysicalCard source) {
        boolean result = true;
        for (Modifier modifier : getModifiers(ModifierEffect.DISCARD_FROM_PLAY_MODIFIER))
            if (affectsCardWithSkipSet(gameState, card, modifier))
                result = modifier.canBeDiscardedFromPlay(gameState, this, card, source, result);
        return result;
    }

    @Override
    public boolean canBeHealed(GameState gameState, PhysicalCard card) {
        boolean result = true;
        for (Modifier modifier : getModifiers(ModifierEffect.WOUND_MODIFIER))
            if (affectsCardWithSkipSet(gameState, card, modifier))
                result = modifier.canBeHealed(gameState, this, card, result);
        return result;
    }

    /**
     * Rule of 4. "You cannot draw (or take into hand) more than 4 cards during your fellowship phase."
     *
     * @param gameState
     * @param playerId
     * @return
     */
    @Override
    public boolean canDrawCardAndIncrement(GameState gameState, String playerId) {
        if (gameState.getCurrentPlayerId().equals(playerId)) {
            if (gameState.getCurrentPhase() != Phase.FELLOWSHIP || _drawnThisPhaseCount < 4) {
                _drawnThisPhaseCount++;
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canLookOrRevealCardsInHand(GameState gameState, String playerId) {
        for (Modifier modifier : getModifiers(ModifierEffect.DISCARD_FROM_PLAY_MODIFIER))
            if (!modifier.canLookOrRevealCardsInHand(gameState, this, playerId))
                return false;
        return true;
    }

    @Override
    public int getSpotCount(GameState gameState, Filter filter, int inPlayCount) {
        int result = inPlayCount;
        for (Modifier modifier : getModifiers(ModifierEffect.SPOT_MODIFIER))
            result = modifier.getSpotCount(gameState, this, filter, result);
        return Math.max(0, result);
    }

    @Override
    public boolean hasFlagActive(ModifierFlag modifierFlag) {
        for (Modifier modifier : getModifiers(ModifierEffect.SPECIAL_FLAG_MODIFIER))
            if (modifier.hasFlagActive(modifierFlag))
                return true;

        return false;
    }

    private class ModifierHookImpl implements ModifierHook {
        private Modifier _modifier;

        private ModifierHookImpl(Modifier modifier) {
            _modifier = modifier;
        }

        @Override
        public void stop() {
            removeModifier(_modifier);
        }
    }
}
