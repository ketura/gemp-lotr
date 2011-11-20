package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.List;
import java.util.Map;

public abstract class AbstractModifier implements Modifier {
    private PhysicalCard _physicalCard;
    private String _text;
    private Filter _affectFilter;
    private Condition _condition;
    private ModifierEffect _effect;

    protected AbstractModifier(PhysicalCard source, String text, Filterable affectFilter, ModifierEffect effect) {
        this(source, text, affectFilter, null, effect);
    }

    protected AbstractModifier(PhysicalCard source, String text, Filterable affectFilter, Condition condition, ModifierEffect effect) {
        _physicalCard = source;
        _text = text;
        _affectFilter = (affectFilter != null) ? Filters.and(affectFilter) : null;
        _condition = condition;
        _effect = effect;
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
    public ModifierEffect getModifierEffect() {
        return _effect;
    }

    @Override
    public boolean affectsCard(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
        return (_condition == null || _condition.isFullfilled(gameState, modifiersQuerying)) && (_affectFilter != null && _affectFilter.accepts(gameState, modifiersQuerying, physicalCard));
    }

    @Override
    public boolean hasRemovedText(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
        return false;
    }

    @Override
    public boolean isKeywordRemoved(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword) {
        return false;
    }

    @Override
    public boolean hasKeyword(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword) {
        return false;
    }

    @Override
    public int getKeywordCountModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword) {
        return 0;
    }

    @Override
    public boolean appliesKeywordModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard modifierSource, Keyword keyword) {
        return true;
    }

    @Override
    public int getStrengthModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
        return 0;
    }

    @Override
    public boolean appliesStrengthModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard modifierSource) {
        return true;
    }

    @Override
    public int getVitalityModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
        return 0;
    }

    @Override
    public int getResistanceModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
        return 0;
    }

    @Override
    public int getMinionSiteNumberModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
        return 0;
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, boolean ignoreRoamingPenalty) {
        return 0;
    }

    @Override
    public int getPlayOnTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, PhysicalCard target) {
        return 0;
    }

    @Override
    public int getOverwhelmMultiplier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
        return 0;
    }

    @Override
    public boolean canTakeWound(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int woundsAlreadyTaken) {
        return true;
    }

    @Override
    public boolean canTakeArcheryWound(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
        return true;
    }

    @Override
    public boolean canBeExerted(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard exertionSource, PhysicalCard card) {
        return true;
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
    public int getArcheryTotalModifier(GameState gameState, ModifiersQuerying modifiersQuerying, Side side) {
        return 0;
    }

    @Override
    public int getMoveLimitModifier(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return 0;
    }

    @Override
    public boolean addsToArcheryTotal(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
        return true;
    }

    @Override
    public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, String performingPlayer, Action action) {
        return true;
    }

    @Override
    public List<? extends ActivateCardAction> getExtraPhaseAction(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
        return null;
    }

    @Override
    public List<? extends Action> getExtraPhaseActionFromStacked(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
        return null;
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
    public boolean shouldSkipPhase(GameState gameState, ModifiersQuerying modifiersQuerying, Phase phase, String playerId) {
        return false;
    }

    @Override
    public boolean isValidAssignments(GameState gameState, Side side, ModifiersQuerying modifiersQuerying, PhysicalCard companion, List<PhysicalCard> minions) {
        return true;
    }

    @Override
    public boolean isValidAssignments(GameState gameState, Side side, ModifiersQuerying modifiersQuerying, Map<PhysicalCard, List<PhysicalCard>> assignments) {
        return true;
    }

    @Override
    public boolean canBeAssignedToSkirmish(GameState gameState, Side sidePlayer, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
        return true;
    }

    @Override
    public boolean canBeDiscardedFromPlay(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card, PhysicalCard source) {
        return true;
    }

    @Override
    public boolean canBeHealed(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
        return true;
    }

    @Override
    public boolean canRemoveBurden(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard source) {
        return true;
    }

    @Override
    public boolean canRemoveThreat(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard source) {
        return true;
    }

    @Override
    public int getRoamingPenaltyModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
        return 0;
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
    public int getSpotCountModifier(GameState gameState, ModifiersQuerying modifiersQuerying, Filter filter) {
        return 0;
    }

    @Override
    public boolean hasFlagActive(GameState gameState, ModifiersQuerying modifiersQuerying, ModifierFlag modifierFlag) {
        return false;
    }

    @Override
    public Side hasInitiative(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return null;
    }

    @Override
    public int getInitiativeHandSizeModifier(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return 0;
    }
}
