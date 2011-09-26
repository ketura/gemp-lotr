package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.List;
import java.util.Map;

public class ProxyingModifier implements Modifier {
    private PhysicalCard _card;
    private Filter _filter;
    private final ModifierEffect[] _modifierEffects = new ModifierEffect[]{ModifierEffect.ALL_MODIFIER};

    public ProxyingModifier(PhysicalCard card, Filter filter) {
        _card = card;
        _filter = filter;
    }

    @Override
    public PhysicalCard getSource() {
        return _card;
    }

    @Override
    public String getText() {
        return "Copy of another card text";
    }

    @Override
    public ModifierEffect[] getModifierEffects() {
        return _modifierEffects;
    }

    private Modifier getProxiedModifier(GameState gameState, ModifiersQuerying modifiersQuerying) {
        PhysicalCard firstActive = Filters.findFirstActive(gameState, modifiersQuerying, _filter);
        if (firstActive != null)
            return firstActive.getBlueprint().getAlwaysOnEffect(_card);
        else
            return null;
    }

    @Override
    public boolean affectsCard(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.affectsCard(gameState, modifiersQuerying, physicalCard);
        return false;
    }

    @Override
    public boolean isKeywordRemoved(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.isKeywordRemoved(gameState, modifiersQuerying, physicalCard, keyword);
        return false;
    }

    @Override
    public boolean hasKeyword(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, boolean result) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.hasKeyword(gameState, modifiersQuerying, physicalCard, keyword, result);
        return result;
    }

    @Override
    public int getKeywordCount(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, int result) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.getKeywordCount(gameState, modifiersQuerying, physicalCard, keyword, result);
        return result;
    }

    @Override
    public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.getStrength(gameState, modifiersQuerying, physicalCard, result);
        return result;
    }

    @Override
    public boolean appliesStrengthModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard modifierSource, boolean result) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.appliesStrengthModifier(gameState, modifiersQuerying, modifierSource, result);
        return result;
    }

    @Override
    public int getVitality(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.getVitality(gameState, modifiersQuerying, physicalCard, result);
        return result;
    }

    @Override
    public int getTwilightCost(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.getTwilightCost(gameState, modifiersQuerying, physicalCard, result);
        return result;
    }

    @Override
    public int getRoamingPenalty(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.getRoamingPenalty(gameState, modifiersQuerying, physicalCard, result);
        return result;
    }

    @Override
    public int getPlayOnTwilightCost(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, PhysicalCard target, int result) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.getPlayOnTwilightCost(gameState, modifiersQuerying, physicalCard, target, result);
        return result;
    }

    @Override
    public boolean isOverwhelmedByStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int strength, int opposingStrength, boolean result) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.isOverwhelmedByStrength(gameState, modifiersQuerying, physicalCard, strength, opposingStrength, result);
        return result;
    }

    @Override
    public boolean canTakeWound(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, boolean result) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.canTakeWound(gameState, modifiersQuerying, physicalCard, result);
        return result;
    }

    @Override
    public boolean isAllyOnCurrentSite(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card, boolean allyOnCurrentSite) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.isAllyOnCurrentSite(gameState, modifiersQuerying, card, allyOnCurrentSite);
        return allyOnCurrentSite;
    }

    @Override
    public int getArcheryTotal(GameState gameState, ModifiersQuerying modifiersQuerying, Side side, int result) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.getArcheryTotal(gameState, modifiersQuerying, side, result);
        return result;
    }

    @Override
    public int getMoveLimit(GameState gameState, ModifiersQuerying modifiersQuerying, int result) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.getMoveLimit(gameState, modifiersQuerying, result);
        return result;
    }

    @Override
    public boolean addsToArcheryTotal(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card, boolean result) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.addsToArcheryTotal(gameState, modifiersQuerying, card, result);
        return result;
    }

    @Override
    public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, Action action, boolean result) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.canPlayAction(gameState, modifiersQuerying, action, result);
        return result;
    }

    @Override
    public boolean shouldSkipPhase(GameState gameState, ModifiersQuerying modifiersQuerying, Phase phase, String playerId, boolean result) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.shouldSkipPhase(gameState, modifiersQuerying, phase, null, result);
        return result;
    }

    @Override
    public boolean isValidFreePlayerAssignments(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard companion, List<PhysicalCard> minions, boolean result) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.isValidFreePlayerAssignments(gameState, modifiersQuerying, companion, minions, result);
        return result;
    }

    @Override
    public boolean isValidFreePlayerAssignments(GameState gameState, ModifiersQuerying modifiersQuerying, Map<PhysicalCard, List<PhysicalCard>> assignments, boolean result) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.isValidFreePlayerAssignments(gameState, modifiersQuerying, assignments, result);
        return result;
    }

    @Override
    public boolean canBeDiscardedFromPlay(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card, PhysicalCard source, boolean result) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.canBeDiscardedFromPlay(gameState, modifiersQuerying, card, source, result);
        return result;
    }

    @Override
    public boolean canBeHealed(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card, boolean result) {
        Modifier modifier = getProxiedModifier(gameState, modifiersQuerying);
        if (modifier != null)
            return modifier.canBeHealed(gameState, modifiersQuerying, card, result);
        return result;
    }
}
