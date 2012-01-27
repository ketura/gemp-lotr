package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.*;

public class ModifiersLogic implements ModifiersEnvironment, ModifiersQuerying {
    private Map<ModifierEffect, List<Modifier>> _modifiers = new HashMap<ModifierEffect, List<Modifier>>();

    private Map<Phase, List<Modifier>> _untilStartOfPhaseModifiers = new HashMap<Phase, List<Modifier>>();
    private Map<Phase, List<Modifier>> _untilEndOfPhaseModifiers = new HashMap<Phase, List<Modifier>>();
    private List<Modifier> _untilEndOfTurnModifiers = new LinkedList<Modifier>();

    private Set<Modifier> _skipSet = new HashSet<Modifier>();

    private Map<Phase, Map<Integer, LimitCounter>> _counters = new HashMap<Phase, Map<Integer, LimitCounter>>();

    private int _drawnThisPhaseCount = 0;
    private Map<Integer, Integer> _woundsPerPhaseMap = new HashMap<Integer, Integer>();

    @Override
    public LimitCounter getUntilEndOfPhaseLimitCounter(PhysicalCard card, Phase phase) {
        Map<Integer, LimitCounter> limitCounterMap = _counters.get(phase);
        if (limitCounterMap == null) {
            limitCounterMap = new HashMap<Integer, LimitCounter>();
            _counters.put(phase, limitCounterMap);
        }
        LimitCounter limitCounter = limitCounterMap.get(card.getCardId());
        if (limitCounter == null) {
            limitCounter = new DefaultLimitCounter();
            limitCounterMap.put(card.getCardId(), limitCounter);
        }
        return limitCounter;
    }

    @Override
    public void addedWound(PhysicalCard card) {
        final int cardId = card.getCardId();
        final Integer previousWounds = _woundsPerPhaseMap.get(cardId);
        if (previousWounds == null)
            _woundsPerPhaseMap.put(cardId, 1);
        else
            _woundsPerPhaseMap.put(cardId, previousWounds + 1);
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
        ModifierEffect modifierEffect = modifier.getModifierEffect();
        getEffectModifiers(modifierEffect).add(modifier);
    }

    private List<Modifier> getModifiers(GameState gameState, ModifierEffect modifierEffect) {
        return getKeywordModifiersAffectingCard(gameState, modifierEffect, null, null);
    }

    private List<Modifier> getModifiersAffectingCard(GameState gameState, ModifierEffect modifierEffect, PhysicalCard card) {
        return getKeywordModifiersAffectingCard(gameState, modifierEffect, null, card);
    }

    private List<Modifier> getKeywordModifiersAffectingCard(GameState gameState, ModifierEffect modifierEffect, Keyword keyword, PhysicalCard card) {
        List<Modifier> modifiers = _modifiers.get(modifierEffect);
        if (modifiers == null)
            return Collections.emptyList();
        else {
            LinkedList<Modifier> liveModifiers = new LinkedList<Modifier>();
            for (Modifier modifier : modifiers) {
                if (keyword == null || ((KeywordAffectingModifier) modifier).getKeyword() == keyword) {
                    if (!_skipSet.contains(modifier)) {
                        _skipSet.add(modifier);
                        Condition condition = modifier.getCondition();
                        if (condition == null || condition.isFullfilled(gameState, this))
                            if (modifierEffect == ModifierEffect.TEXT_MODIFIER || modifier.getSource() == null || !hasTextRemoved(gameState, modifier.getSource())) {
                                if (card == null || modifier.affectsCard(gameState, this, card))
                                    liveModifiers.add(modifier);
                            }
                        _skipSet.remove(modifier);
                    }
                }
            }

            return liveModifiers;
        }
    }

    public void removeEndOfPhase(Phase phase) {
        List<Modifier> list = _untilEndOfPhaseModifiers.get(phase);
        if (list != null) {
            removeModifiers(list);
            list.clear();
        }
        Map<Integer, LimitCounter> counterMap = _counters.get(phase);
        if (counterMap != null)
            counterMap.clear();

        _drawnThisPhaseCount = 0;
        _woundsPerPhaseMap.clear();
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
                Condition condition = modifier.getCondition();
                if (condition == null || condition.isFullfilled(gameState, this))
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
    public boolean hasTextRemoved(GameState gameState, PhysicalCard card) {
        for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.TEXT_MODIFIER, card)) {
            if (modifier.hasRemovedText(gameState, this, card))
                return true;
        }
        return false;
    }

    @Override
    public boolean hasKeyword(GameState gameState, PhysicalCard physicalCard, Keyword keyword) {
        LoggingThreadLocal.logMethodStart(physicalCard, "hasKeyword " + keyword.getHumanReadable());
        try {
            if (isCandidateForKeywordRemovalWithTextRemoval(gameState, physicalCard, keyword) && hasTextRemoved(gameState, physicalCard))
                return false;

            for (Modifier modifier : getKeywordModifiersAffectingCard(gameState, ModifierEffect.REMOVE_KEYWORD_MODIFIER, keyword, physicalCard)) {
                if (modifier.isKeywordRemoved(gameState, this, physicalCard, keyword))
                    return false;
            }

            if (physicalCard.getBlueprint().hasKeyword(keyword))
                return true;

            for (Modifier modifier : getKeywordModifiersAffectingCard(gameState, ModifierEffect.GIVE_KEYWORD_MODIFIER, keyword, physicalCard)) {
                if (appliesKeywordModifier(gameState, physicalCard, modifier.getSource(), keyword))
                    if (modifier.hasKeyword(gameState, this, physicalCard, keyword))
                        return true;
            }
            return false;
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public int getKeywordCount(GameState gameState, PhysicalCard physicalCard, Keyword keyword) {
        LoggingThreadLocal.logMethodStart(physicalCard, "getKeywordCount " + keyword.getHumanReadable());
        try {
            if (isCandidateForKeywordRemovalWithTextRemoval(gameState, physicalCard, keyword) && hasTextRemoved(gameState, physicalCard))
                return 0;

            for (Modifier modifier : getKeywordModifiersAffectingCard(gameState, ModifierEffect.REMOVE_KEYWORD_MODIFIER, keyword, physicalCard)) {
                if (modifier.isKeywordRemoved(gameState, this, physicalCard, keyword))
                    return 0;
            }

            int result = physicalCard.getBlueprint().getKeywordCount(keyword);
            for (Modifier modifier : getKeywordModifiersAffectingCard(gameState, ModifierEffect.GIVE_KEYWORD_MODIFIER, keyword, physicalCard)) {
                if (appliesKeywordModifier(gameState, physicalCard, modifier.getSource(), keyword))
                    result += modifier.getKeywordCountModifier(gameState, this, physicalCard, keyword);
            }
            return Math.max(0, result);
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    private boolean isCandidateForKeywordRemovalWithTextRemoval(GameState gameState, PhysicalCard physicalCard, Keyword keyword) {
        if (keyword == Keyword.ROAMING)
            return false;
        if (keyword == Keyword.RING_BOUND)
            if (gameState.getRingBearer(physicalCard.getOwner()) == physicalCard)
                return false;
        return true;
    }

    private boolean appliesKeywordModifier(GameState gameState, PhysicalCard affecting, PhysicalCard modifierSource, Keyword keyword) {
        if (modifierSource == null)
            return true;
        for (Modifier modifier : getKeywordModifiersAffectingCard(gameState, ModifierEffect.CANCEL_KEYWORD_BONUS_TARGET_MODIFIER, keyword, affecting)) {
            if (!modifier.appliesKeywordModifier(gameState, this, modifierSource, keyword))
                return false;
        }
        return true;
    }

    @Override
    public boolean hasSignet(GameState gameState, PhysicalCard physicalCard, Signet signet) {
        LoggingThreadLocal.logMethodStart(physicalCard, "hasSignet " + signet);
        try {
            if (physicalCard.getBlueprint().getSignet() == signet)
                return true;
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.SIGNET_MODIFIER, physicalCard)) {
                if (modifier.hasSignet(gameState, this, physicalCard, signet))
                    return true;
            }
            return false;
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public int getArcheryTotal(GameState gameState, Side side, int baseArcheryTotal) {
        int result = baseArcheryTotal;
        for (Modifier modifier : getModifiers(gameState, ModifierEffect.ARCHERY_MODIFIER))
            result += modifier.getArcheryTotalModifier(gameState, this, side);
        return Math.max(0, result);
    }

    @Override
    public int getMoveLimit(GameState gameState, int baseMoveLimit) {
        int result = baseMoveLimit;
        for (Modifier modifier : getModifiers(gameState, ModifierEffect.MOVE_LIMIT_MODIFIER))
            result += modifier.getMoveLimitModifier(gameState, this);
        return Math.max(1, result);
    }

    @Override
    public int getStrength(GameState gameState, PhysicalCard physicalCard) {
        LoggingThreadLocal.logMethodStart(physicalCard, "getStrength");
        try {
            int result = physicalCard.getBlueprint().getStrength();
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.STRENGTH_MODIFIER, physicalCard)) {
                final int strengthModifier = modifier.getStrengthModifier(gameState, this, physicalCard);
                if (strengthModifier <= 0 || appliesStrengthBonusModifier(gameState, modifier.getSource(), physicalCard))
                    result += strengthModifier;
            }
            return Math.max(0, result);
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    private boolean appliesStrengthBonusModifier(GameState gameState, PhysicalCard modifierSource, PhysicalCard modifierTarget) {
        if (modifierSource != null)
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.STRENGTH_BONUS_SOURCE_MODIFIER, modifierSource)) {
                if (!modifier.appliesStrengthBonusModifier(gameState, this, modifierSource, modifierTarget))
                    return false;
            }
        if (modifierTarget != null)
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.STRENGTH_BONUS_TARGET_MODIFIER, modifierTarget)) {
                if (!modifier.appliesStrengthBonusModifier(gameState, this, modifierSource, modifierTarget))
                    return false;
            }
        return true;
    }

    @Override
    public int getVitality(GameState gameState, PhysicalCard physicalCard) {
        LoggingThreadLocal.logMethodStart(physicalCard, "getVitality");
        try {
            int result = physicalCard.getBlueprint().getVitality() - gameState.getWounds(physicalCard);
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.VITALITY_MODIFIER, physicalCard)) {
                result += modifier.getVitalityModifier(gameState, this, physicalCard);
            }
            return Math.max(0, result);
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public int getResistance(GameState gameState, PhysicalCard physicalCard) {
        LoggingThreadLocal.logMethodStart(physicalCard, "getResistance");
        try {
            int result = physicalCard.getBlueprint().getResistance();
            // Companions resistance is reduced by the number of burdens
            if (physicalCard.getBlueprint().getCardType() == CardType.COMPANION)
                result -= gameState.getBurdens();
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.RESISTANCE_MODIFIER, physicalCard)) {
                result += modifier.getResistanceModifier(gameState, this, physicalCard);
            }
            return Math.max(0, result);
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public int getMinionSiteNumber(GameState gameState, PhysicalCard physicalCard) {
        LoggingThreadLocal.logMethodStart(physicalCard, "getMinionSiteNumber");
        try {
            int result = physicalCard.getBlueprint().getSiteNumber();
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.SITE_NUMBER_MODIFIER, physicalCard)) {
                result += modifier.getMinionSiteNumberModifier(gameState, this, physicalCard);
            }
            return Math.max(0, result);
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public int getTwilightCost(GameState gameState, PhysicalCard physicalCard, boolean ignoreRoamingPenalty) {
        LoggingThreadLocal.logMethodStart(physicalCard, "getTwilightCost");
        try {
            int result = physicalCard.getBlueprint().getTwilightCost();
            result += physicalCard.getBlueprint().getTwilightCostModifier(gameState, this, physicalCard);
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.TWILIGHT_COST_MODIFIER, physicalCard)) {
                result += modifier.getTwilightCostModifier(gameState, this, physicalCard, ignoreRoamingPenalty);
            }
            return Math.max(0, result);
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public int getPlayOnTwilightCost(GameState gameState, PhysicalCard physicalCard, PhysicalCard target) {
        LoggingThreadLocal.logMethodStart(physicalCard, "getPlayOnTwilightCost");
        try {
            int result = getTwilightCost(gameState, physicalCard, false);
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.TWILIGHT_COST_MODIFIER, physicalCard)) {
                result += modifier.getPlayOnTwilightCostModifier(gameState, this, physicalCard, target);
            }
            return Math.max(0, result);
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public int getRoamingPenalty(GameState gameState, PhysicalCard physicalCard) {
        LoggingThreadLocal.logMethodStart(physicalCard, "getRoamingPenalty");
        try {
            int result = 2;
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.TWILIGHT_COST_MODIFIER, physicalCard)) {
                result += modifier.getRoamingPenaltyModifier(gameState, this, physicalCard);
            }
            return Math.max(0, result);
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public int getOverwhelmMultiplier(GameState gameState, PhysicalCard card) {
        LoggingThreadLocal.logMethodStart(card, "getOverwhelmMultiplier");
        try {
            int overwhelmMultiplier = 2;
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.OVERWHELM_MODIFIER, card)) {
                overwhelmMultiplier = Math.max(overwhelmMultiplier, modifier.getOverwhelmMultiplier(gameState, this, card));
            }
            return overwhelmMultiplier;
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public boolean isAdditionalCardType(GameState gameState, PhysicalCard card, CardType cardType) {
        for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.ADDITIONAL_CARD_TYPE, card))
            if (modifier.isAdditionalCardTypeModifier(gameState, this, card, cardType))
                return true;
        return false;
    }

    @Override
    public int getWoundsTakenInCurrentPhase(PhysicalCard card) {
        Integer wounds = _woundsPerPhaseMap.get(card.getCardId());
        if (wounds == null)
            return 0;
        return wounds;
    }

    @Override
    public boolean canTakeWounds(GameState gameState, PhysicalCard card, int woundsToTake) {
        LoggingThreadLocal.logMethodStart(card, "canTakeWound");
        try {
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.WOUND_MODIFIER, card)) {
                Integer woundsTaken = _woundsPerPhaseMap.get(card.getCardId());
                if (woundsTaken == null)
                    woundsTaken = 0;
                if (!modifier.canTakeWounds(gameState, this, card, woundsTaken, woundsToTake))
                    return false;
            }
            return true;
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public boolean canTakeWoundsFromLosingSkirmish(GameState gameState, PhysicalCard card, Set<PhysicalCard> winners) {
        LoggingThreadLocal.logMethodStart(card, "canTakeWound");
        try {
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.WOUND_MODIFIER, card)) {
                if (!modifier.canTakeWoundsFromLosingSkirmish(gameState, this, card, winners))
                    return false;
            }
            return true;
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public boolean canTakeArcheryWound(GameState gameState, PhysicalCard card) {
        LoggingThreadLocal.logMethodStart(card, "canTakeArcheryWound");
        try {
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.WOUND_MODIFIER, card)) {
                if (!modifier.canTakeArcheryWound(gameState, this, card))
                    return false;
            }
            return true;
        } finally

        {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public boolean canBeExerted(GameState gameState, PhysicalCard source, PhysicalCard card) {
        LoggingThreadLocal.logMethodStart(card, "canBeExerted");
        try {
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.WOUND_MODIFIER, card)) {
                if (!modifier.canBeExerted(gameState, this, source, card))
                    return false;
            }
            return true;
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public boolean isUnhastyCompanionAllowedToParticipateInSkirmishes(GameState gameState, PhysicalCard card) {
        LoggingThreadLocal.logMethodStart(card, "isAllyAllowedToParticipateInSkirmishes");
        try {
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.PRESENCE_MODIFIER, card)) {
                if (modifier.isUnhastyCompanionAllowedToParticipateInSkirmishes(gameState, this, card))
                    return true;
            }
            return false;
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public boolean isAllyAllowedToParticipateInArcheryFire(GameState gameState, PhysicalCard card) {
        LoggingThreadLocal.logMethodStart(card, "isAllyAllowedToParticipateInArcheryFire");
        try {
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.PRESENCE_MODIFIER, card)) {
                if (modifier.isAllyParticipateInArcheryFire(gameState, this, card))
                    return true;
            }
            return false;
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public boolean isAllyAllowedToParticipateInSkirmishes(GameState gameState, Side sidePlayer, PhysicalCard card) {
        LoggingThreadLocal.logMethodStart(card, "isAllyAllowedToParticipateInSkirmishes");
        try {
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.PRESENCE_MODIFIER, card)) {
                if (modifier.isAllyParticipateInSkirmishes(gameState, sidePlayer, this, card))
                    return true;
            }
            return false;
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public boolean isAllyPreventedFromParticipatingInArcheryFire(GameState gameState, PhysicalCard card) {
        LoggingThreadLocal.logMethodStart(card, "isAllyPreventedFromParticipatingInArcheryFire");
        try {
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.PRESENCE_MODIFIER, card)) {
                if (modifier.isAllyPreventedFromParticipatingInArcheryFire(gameState, this, card))
                    return true;
            }
            return false;
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public boolean isAllyPreventedFromParticipatingInSkirmishes(GameState gameState, Side sidePlayer, PhysicalCard card) {
        LoggingThreadLocal.logMethodStart(card, "isAllyPreventedFromParticipatingInSkirmishes");
        try {
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.PRESENCE_MODIFIER, card)) {
                if (modifier.isAllyPreventedFromParticipatingInSkirmishes(gameState, sidePlayer, this, card))
                    return true;
            }
            return false;
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public boolean addsToArcheryTotal(GameState gameState, PhysicalCard card) {
        LoggingThreadLocal.logMethodStart(card, "addsToArcheryTotal");
        try {
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.ARCHERY_MODIFIER, card))
                if (!modifier.addsToArcheryTotal(gameState, this, card))
                    return false;

            return true;
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public boolean canPlayAction(GameState gameState, String performingPlayer, Action action) {
        for (Modifier modifier : getModifiers(gameState, ModifierEffect.ACTION_MODIFIER))
            if (!modifier.canPlayAction(gameState, this, performingPlayer, action))
                return false;
        return true;
    }

    @Override
    public boolean canHavePlayedOn(GameState gameState, PhysicalCard playedCard, PhysicalCard target) {
        for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.TARGET_MODIFIER, target))
            if (!modifier.canHavePlayedOn(gameState, this, playedCard, target))
                return false;
        return true;
    }

    @Override
    public boolean canHaveTransferredOn(GameState gameState, PhysicalCard playedCard, PhysicalCard target) {
        for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.TARGET_MODIFIER, target))
            if (!modifier.canHaveTransferredOn(gameState, this, playedCard, target))
                return false;
        return true;
    }

    @Override
    public boolean shouldSkipPhase(GameState gameState, Phase phase, String playerId) {
        for (Modifier modifier : getModifiers(gameState, ModifierEffect.ACTION_MODIFIER))
            if (modifier.shouldSkipPhase(gameState, this, phase, playerId))
                return true;
        return false;
    }

    @Override
    public List<? extends ActivateCardAction> getExtraPhaseActions(GameState gameState, PhysicalCard target) {
        List<ActivateCardAction> activateCardActions = new LinkedList<ActivateCardAction>();

        for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.EXTRA_ACTION_MODIFIER, target)) {
            List<? extends ActivateCardAction> actions = modifier.getExtraPhaseAction(gameState, this, target);
            if (actions != null)
                activateCardActions.addAll(actions);
        }

        return activateCardActions;
    }

    @Override
    public List<? extends Action> getExtraPhaseActionsFromStacked(GameState gameState, PhysicalCard target) {
        List<Action> activateCardActions = new LinkedList<Action>();

        for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.EXTRA_ACTION_MODIFIER, target)) {
            List<? extends Action> actions = modifier.getExtraPhaseActionFromStacked(gameState, this, target);
            if (actions != null)
                activateCardActions.addAll(actions);
        }

        return activateCardActions;
    }

    @Override
    public boolean isValidAssignments(GameState gameState, Side side, Map<PhysicalCard, Set<PhysicalCard>> assignments) {
        for (Modifier modifier : getModifiers(gameState, ModifierEffect.ASSIGNMENT_MODIFIER)) {
            if (!modifier.isValidAssignments(gameState, side, this, assignments))
                return false;
            for (Map.Entry<PhysicalCard, Set<PhysicalCard>> assignment : assignments.entrySet()) {
                if (affectsCardWithSkipSet(gameState, assignment.getKey(), modifier))
                    if (!modifier.isValidAssignments(gameState, side, this, assignment.getKey(), assignment.getValue()))
                        return false;
            }
        }
        return true;
    }

    @Override
    public boolean canBeAssignedToSkirmish(GameState gameState, Side sidePlayer, PhysicalCard card) {
        LoggingThreadLocal.logMethodStart(card, "canBeAssignedToSkirmish");
        try {
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.ASSIGNMENT_MODIFIER, card))
                if (modifier.isPreventedFromBeingAssignedToSkirmish(gameState, sidePlayer, this, card))
                    return false;
            return true;
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public boolean canBeDiscardedFromPlay(GameState gameState, PhysicalCard card, PhysicalCard source) {
        LoggingThreadLocal.logMethodStart(card, "canBeDiscardedFromPlay");
        try {
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.DISCARD_FROM_PLAY_MODIFIER, card))
                if (!modifier.canBeDiscardedFromPlay(gameState, this, card, source))
                    return false;
            return true;
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public boolean canBeReturnedToHand(GameState gameState, PhysicalCard card, PhysicalCard source) {
        LoggingThreadLocal.logMethodStart(card, "canBeReturnedToHand");
        try {
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.RETURN_TO_HAND_MODIFIER, card))
                if (!modifier.canBeReturnedToHand(gameState, this, card, source))
                    return false;
            return true;
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public boolean canBeHealed(GameState gameState, PhysicalCard card) {
        LoggingThreadLocal.logMethodStart(card, "canBeHealed");
        try {
            for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.WOUND_MODIFIER, card))
                if (!modifier.canBeHealed(gameState, this, card))
                    return false;
            return true;
        } finally {
            LoggingThreadLocal.logMethodEnd();
        }
    }

    @Override
    public boolean canRemoveBurden(GameState gameState, PhysicalCard source) {
        for (Modifier modifier : getModifiers(gameState, ModifierEffect.BURDEN_MODIFIER)) {
            if (!modifier.canRemoveBurden(gameState, this, source))
                return false;
        }
        return true;
    }

    @Override
    public boolean canRemoveThreat(GameState gameState, PhysicalCard source) {
        for (Modifier modifier : getModifiers(gameState, ModifierEffect.THREAT_MODIFIER)) {
            if (!modifier.canRemoveThreat(gameState, this, source))
                return false;
        }
        return true;
    }

    /**
     * Rule of 4. "You cannot draw (or take into hand) more than 4 cards during your fellowship phase."
     *
     * @param gameState
     * @param playerId
     * @return
     */
    @Override
    public boolean canDrawCardNoIncrement(GameState gameState, String playerId) {
        if (gameState.getCurrentPlayerId().equals(playerId)) {
            if (gameState.getCurrentPhase() != Phase.FELLOWSHIP)
                return true;
            if (gameState.getCurrentPhase() == Phase.FELLOWSHIP && _drawnThisPhaseCount < 4) {
                return true;
            } else {
                return false;
            }
        }
        return true;
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
            if (gameState.getCurrentPhase() != Phase.FELLOWSHIP)
                return true;
            if (gameState.getCurrentPhase() == Phase.FELLOWSHIP && _drawnThisPhaseCount < 4) {
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
        for (Modifier modifier : getModifiers(gameState, ModifierEffect.LOOK_OR_REVEAL_MODIFIER))
            if (!modifier.canLookOrRevealCardsInHand(gameState, this, playerId))
                return false;
        return true;
    }

    @Override
    public boolean canDiscardCardsFromHand(GameState gameState, String playerId, PhysicalCard source) {
        for (Modifier modifier : getModifiers(gameState, ModifierEffect.DISCARD_NOT_FROM_PLAY))
            if (!modifier.canDiscardCardsFromHand(gameState, this, playerId, source))
                return false;
        return true;
    }

    @Override
    public boolean canDiscardCardsFromTopOfDeck(GameState gameState, String playerId, PhysicalCard source) {
        for (Modifier modifier : getModifiers(gameState, ModifierEffect.DISCARD_NOT_FROM_PLAY))
            if (!modifier.canDiscardCardsFromTopOfDeck(gameState, this, playerId, source))
                return false;
        return true;
    }

    @Override
    public Side hasInitiative(GameState gameState) {
        for (Modifier modifier : getModifiers(gameState, ModifierEffect.INITIATIVE_MODIFIER)) {
            Side initiative = modifier.hasInitiative(gameState, this);
            if (initiative != null)
                return initiative;
        }

        int freePeopleInitiativeHandSize = gameState.getHand(gameState.getCurrentPlayerId()).size()
                + gameState.getVoid(gameState.getCurrentPlayerId()).size();

        int initiativeHandSize = 4;
        for (Modifier modifier : getModifiers(gameState, ModifierEffect.INITIATIVE_MODIFIER))
            initiativeHandSize += modifier.getInitiativeHandSizeModifier(gameState, this);

        if (freePeopleInitiativeHandSize < initiativeHandSize)
            return Side.SHADOW;
        else
            return Side.FREE_PEOPLE;
    }

    @Override
    public int getSpotCount(GameState gameState, Filter filter, int inPlayCount) {
        int result = inPlayCount;
        for (Modifier modifier : getModifiers(gameState, ModifierEffect.SPOT_MODIFIER))
            result += modifier.getSpotCountModifier(gameState, this, filter);
        return Math.max(0, result);
    }

    @Override
    public boolean hasFlagActive(GameState gameState, ModifierFlag modifierFlag) {
        for (Modifier modifier : getModifiers(gameState, ModifierEffect.SPECIAL_FLAG_MODIFIER))
            if (modifier.hasFlagActive(gameState, this, modifierFlag))
                return true;

        return false;
    }

    @Override
    public boolean canReplaceSite(GameState gameState, String playerId, PhysicalCard siteToReplace) {
        for (Modifier modifier : getModifiersAffectingCard(gameState, ModifierEffect.REPLACE_SITE_MODIFIER, siteToReplace))
            if (!modifier.isSiteReplaceable(gameState, this, playerId))
                return false;

        return true;
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
