package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.timing.Action;

import java.util.List;
import java.util.Map;

public abstract class AbstractModifier implements Modifier {
    private PhysicalCard _physicalCard;
    private String _text;
    private Filter _affectFilter;
    private Condition _condition;
    private ModifierEffect[] _effects;

    protected AbstractModifier(PhysicalCard source, String text, Filterable affectFilter, ModifierEffect[] effects) {
        this(source, text, affectFilter, null, effects);
    }

    protected AbstractModifier(PhysicalCard source, String text, Filterable affectFilter, Condition condition, ModifierEffect[] effects) {
        _physicalCard = source;
        _text = text;
        _affectFilter = (affectFilter != null) ? Filters.and(affectFilter) : null;
        _condition = condition;
        _effects = effects;
    }

    @Override
    public PhysicalCard getSource() {
        return _physicalCard;
    }

    @Override
    public String getText(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return _text;
    }

    @Override
    public ModifierEffect[] getModifierEffects() {
        return _effects;
    }

    @Override
    public boolean affectsCard(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
        return (_condition == null || _condition.isFullfilled(gameState, modifiersQuerying)) && (_affectFilter != null && _affectFilter.accepts(gameState, modifiersQuerying, physicalCard));
    }

    @Override
    public boolean isKeywordRemoved(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword) {
        return false;
    }

    @Override
    public boolean hasKeyword(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, boolean result) {
        return result;
    }

    @Override
    public int getKeywordCount(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, int result) {
        return result;
    }

    @Override
    public boolean appliesKeywordModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard modifierSource, Keyword keyword, boolean result) {
        return result;
    }

    @Override
    public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
        return result;
    }

    @Override
    public boolean appliesStrengthModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard modifierSource, boolean result) {
        return result;
    }

    @Override
    public int getVitality(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
        return result;
    }

    @Override
    public int getResistance(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
        return result;
    }

    @Override
    public int getMinionSiteNumber(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
        return result;
    }

    @Override
    public int getTwilightCost(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
        return result;
    }

    @Override
    public int getPlayOnTwilightCost(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, PhysicalCard target, int result) {
        return result;
    }

    @Override
    public boolean isOverwhelmedByStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int strength, int opposingStrength, boolean result) {
        return result;
    }

    @Override
    public boolean canTakeWound(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, boolean result) {
        return result;
    }

    @Override
    public boolean canBeExerted(GameState gameState, ModifiersLogic modifiersLogic, PhysicalCard exertionSource, PhysicalCard card, boolean result) {
        return result;
    }

    @Override
    public boolean isAllyParticipateInArcheryFire(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
        return false;
    }

    @Override
    public boolean isParticipateInSkirmishes(GameState gameState, Side sidePlayer, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
        return false;
    }

    @Override
    public boolean isAllyPreventedFromParticipatingInArcheryFire(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
        return false;
    }

    @Override
    public boolean isAllyPreventedFromParticipatingInSkirmishes(GameState gameState, Side sidePlayer, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
        return false;
    }

    @Override
    public int getArcheryTotal(GameState gameState, ModifiersQuerying modifiersQuerying, Side side, int result) {
        return result;
    }

    @Override
    public int getMoveLimit(GameState gameState, ModifiersQuerying modifiersQuerying, int result) {
        return result;
    }

    @Override
    public boolean addsToArcheryTotal(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card, boolean result) {
        return result;
    }

    @Override
    public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, String performingPlayer, Action action, boolean result) {
        return result;
    }

    @Override
    public boolean canHavePlayedOn(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard playedCard, PhysicalCard target) {
        return true;
    }

    @Override
    public boolean canHaveTransferredOn(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard playedCard, PhysicalCard target) {
        return true;
    }

    @Override
    public boolean shouldSkipPhase(GameState gameState, ModifiersQuerying modifiersQuerying, Phase phase, String playerId, boolean result) {
        return result;
    }

    @Override
    public boolean isValidAssignments(GameState gameState, Side side, ModifiersQuerying modifiersQuerying, PhysicalCard companion, List<PhysicalCard> minions, boolean result) {
        return result;
    }

    @Override
    public boolean isValidAssignments(GameState gameState, Side side, ModifiersQuerying modifiersQuerying, Map<PhysicalCard, List<PhysicalCard>> assignments, boolean result) {
        return result;
    }

    @Override
    public boolean canBeAssignedToSkirmish(GameState gameState, Side sidePlayer, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
        return true;
    }

    @Override
    public boolean canBeDiscardedFromPlay(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card, PhysicalCard source, boolean result) {
        return result;
    }

    @Override
    public boolean canBeHealed(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card, boolean result) {
        return result;
    }

    @Override
    public int getRoamingPenalty(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
        return result;
    }

    @Override
    public boolean canLookOrRevealCardsInHand(GameState gameState, ModifiersQuerying modifiersQuerying, String playerId) {
        return true;
    }

    @Override
    public boolean canDiscardCardsFromHand(GameState gameState, ModifiersQuerying modifiersQuerying, String playerId, PhysicalCard source) {
        return true;
    }

    @Override
    public boolean canDiscardCardsFromTopOfDeck(GameState gameState, ModifiersQuerying modifiersQuerying, String playerId, PhysicalCard source) {
        return true;
    }

    @Override
    public int getSpotCount(GameState gameState, ModifiersQuerying modifiersQuerying, Filter filter, int result) {
        return result;
    }

    @Override
    public boolean hasFlagActive(ModifierFlag modifierFlag) {
        return false;
    }
}
