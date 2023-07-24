package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.game.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.game.actions.Action;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractModifier implements Modifier {
    private final LotroPhysicalCard _physicalCard;
    private final String _text;
    private final Filter _affectFilter;
    private final Condition _condition;
    private final ModifierEffect _effect;

    protected AbstractModifier(LotroPhysicalCard source, String text, Filterable affectFilter, ModifierEffect effect) {
        this(source, text, affectFilter, null, effect);
    }

    protected AbstractModifier(LotroPhysicalCard source, String text, Filterable affectFilter, Condition condition, ModifierEffect effect) {
        _physicalCard = source;
        _text = text;
        _affectFilter = (affectFilter != null) ? Filters.and(affectFilter) : null;
        _condition = condition;
        _effect = effect;
    }

    @Override
    public boolean isNonCardTextModifier() {
        return false;
    }

    @Override
    public Condition getCondition() {
        return _condition;
    }

    @Override
    public LotroPhysicalCard getSource() {
        return _physicalCard;
    }

    @Override
    public boolean isExtraPossessionClass(DefaultGame game, LotroPhysicalCard physicalCard, LotroPhysicalCard attachedTo) {
        return false;
    }

    @Override
    public String getText(DefaultGame game, LotroPhysicalCard self) {
        return _text;
    }

    @Override
    public ModifierEffect getModifierEffect() {
        return _effect;
    }

    @Override
    public boolean affectsCard(DefaultGame game, LotroPhysicalCard physicalCard) {
        return (_affectFilter != null && _affectFilter.accepts(game, physicalCard));
    }

    @Override
    public boolean hasRemovedText(DefaultGame game, LotroPhysicalCard physicalCard) {
        return false;
    }

    @Override
    public boolean isKeywordRemoved(DefaultGame game, LotroPhysicalCard physicalCard, Keyword keyword) {
        return false;
    }

    @Override
    public boolean hasKeyword(DefaultGame game, LotroPhysicalCard physicalCard, Keyword keyword) {
        return false;
    }

    @Override
    public int getKeywordCountModifier(DefaultGame game, LotroPhysicalCard physicalCard, Keyword keyword) {
        return 0;
    }

    @Override
    public boolean appliesKeywordModifier(DefaultGame game, LotroPhysicalCard modifierSource, Keyword keyword) {
        return true;
    }

    @Override
    public boolean hasSignet(DefaultGame game, LotroPhysicalCard physicalCard, Signet signet) {
        return false;
    }

    @Override
    public int getStrengthModifier(DefaultGame game, LotroPhysicalCard physicalCard) {
        return 0;
    }

    @Override
    public boolean appliesStrengthBonusModifier(DefaultGame game, LotroPhysicalCard modifierSource, LotroPhysicalCard modifierTaget) {
        return true;
    }

    @Override
    public int getVitalityModifier(DefaultGame game, LotroPhysicalCard physicalCard) {
        return 0;
    }

    @Override
    public int getResistanceModifier(DefaultGame game, LotroPhysicalCard physicalCard) {
        return 0;
    }

    @Override
    public int getMinionSiteNumberModifier(DefaultGame game, LotroPhysicalCard physicalCard) {
        return 0;
    }

    @Override
    public boolean isAdditionalCardTypeModifier(DefaultGame game, LotroPhysicalCard physicalCard, CardType cardType) {
        return false;
    }

    @Override
    public int getTwilightCostModifier(DefaultGame game, LotroPhysicalCard physicalCard, LotroPhysicalCard target, boolean ignoreRoamingPenalty) {
        return 0;
    }

    @Override
    public int getOverwhelmMultiplier(DefaultGame game, LotroPhysicalCard physicalCard) {
        return 0;
    }

    @Override
    public boolean canCancelSkirmish(DefaultGame game, LotroPhysicalCard physicalCard) { return true; }

    @Override
    public boolean canTakeWounds(DefaultGame game, Collection<LotroPhysicalCard> woundSources, LotroPhysicalCard physicalCard, int woundsAlreadyTaken, int woundsToTake) {
        return true;
    }

    @Override
    public boolean canTakeWoundsFromLosingSkirmish(DefaultGame game, LotroPhysicalCard physicalCard, Set<LotroPhysicalCard> winners) {
        return true;
    }

    @Override
    public boolean canTakeArcheryWound(DefaultGame game, LotroPhysicalCard physicalCard) {
        return true;
    }

    @Override
    public boolean canBeExerted(DefaultGame game, LotroPhysicalCard exertionSource, LotroPhysicalCard exertedCard) {
        return true;
    }

    @Override
    public boolean isAllyParticipateInArcheryFire(DefaultGame game, LotroPhysicalCard card) {
        return false;
    }

    @Override
    public boolean isAllyParticipateInSkirmishes(DefaultGame game, Side sidePlayer, LotroPhysicalCard card) {
        return false;
    }

    @Override
    public boolean isUnhastyCompanionAllowedToParticipateInSkirmishes(DefaultGame game, LotroPhysicalCard card) {
        return false;
    }

    @Override
    public boolean isAllyPreventedFromParticipatingInArcheryFire(DefaultGame game, LotroPhysicalCard card) {
        return false;
    }

    @Override
    public boolean isAllyPreventedFromParticipatingInSkirmishes(DefaultGame game, Side sidePlayer, LotroPhysicalCard card) {
        return false;
    }

    @Override
    public int getArcheryTotalModifier(DefaultGame game, Side side) {
        return 0;
    }

    @Override
    public int getMoveLimitModifier(DefaultGame game) {
        return 0;
    }

    @Override
    public boolean addsTwilightForCompanionMove(DefaultGame game, LotroPhysicalCard companion) {
        return true;
    }

    @Override
    public boolean addsToArcheryTotal(DefaultGame game, LotroPhysicalCard card) {
        return true;
    }

    @Override
    public boolean canPlayAction(DefaultGame game, String performingPlayer, Action action) {
        return true;
    }

    @Override
    public boolean canPlayCard(DefaultGame game, String performingPlayer, LotroPhysicalCard card) {
        return true;
    }

    @Override
    public List<? extends Action> getExtraPhaseAction(DefaultGame game, LotroPhysicalCard card) {
        return null;
    }

    @Override
    public List<? extends Action> getExtraPhaseActionFromStacked(DefaultGame game, LotroPhysicalCard card) {
        return null;
    }

    @Override
    public boolean canPayExtraCostsToPlay(DefaultGame game, LotroPhysicalCard card) {
        return true;
    }

    @Override
    public void appendExtraCosts(DefaultGame game, CostToEffectAction action, LotroPhysicalCard card) {

    }

    @Override
    public boolean canHavePlayedOn(DefaultGame game, LotroPhysicalCard playedCard, LotroPhysicalCard target) {
        return true;
    }

    @Override
    public boolean canHaveTransferredOn(DefaultGame game, LotroPhysicalCard playedCard, LotroPhysicalCard target) {
        return true;
    }

    @Override
    public boolean canBeTransferred(DefaultGame game, LotroPhysicalCard attachment) {
        return true;
    }

    @Override
    public boolean shouldSkipPhase(DefaultGame game, Phase phase, String playerId) {
        return false;
    }

    @Override
    public boolean isValidAssignments(DefaultGame game, Side side, LotroPhysicalCard companion, Set<LotroPhysicalCard> minions) {
        return true;
    }

    @Override
    public boolean isValidAssignments(DefaultGame game, Side side, Map<LotroPhysicalCard, Set<LotroPhysicalCard>> assignments) {
        return true;
    }

    @Override
    public boolean isPreventedFromBeingAssignedToSkirmish(DefaultGame game, Side sidePlayer, LotroPhysicalCard card) {
        return false;
    }

    @Override
    public boolean canBeDiscardedFromPlay(DefaultGame game, String performingPlayer, LotroPhysicalCard card, LotroPhysicalCard source) {
        return true;
    }

    @Override
    public boolean canBeLiberated(DefaultGame game, String performingPlayer, LotroPhysicalCard card, LotroPhysicalCard source) {
        return true;
    }

    @Override
    public boolean canBeReturnedToHand(DefaultGame game, LotroPhysicalCard card, LotroPhysicalCard source) {
        return true;
    }

    @Override
    public boolean canBeHealed(DefaultGame game, LotroPhysicalCard card) {
        return true;
    }

    @Override
    public boolean canAddBurden(DefaultGame game, String performingPlayer, LotroPhysicalCard source) {
        return true;
    }

    @Override
    public boolean canRemoveBurden(DefaultGame game, LotroPhysicalCard source) {
        return true;
    }

    @Override
    public boolean canRemoveThreat(DefaultGame game, LotroPhysicalCard source) {
        return true;
    }

    @Override
    public int getRoamingPenaltyModifier(DefaultGame game, LotroPhysicalCard physicalCard) {
        return 0;
    }

    @Override
    public boolean canLookOrRevealCardsInHand(DefaultGame game, String revealingPlayerId, String actingPlayerId) {
        return true;
    }

    @Override
    public boolean canDiscardCardsFromHand(DefaultGame game, String playerId, LotroPhysicalCard source) {
        return true;
    }

    @Override
    public boolean canDiscardCardsFromTopOfDeck(DefaultGame game, String playerId, LotroPhysicalCard source) {
        return true;
    }

    @Override
    public int getSpotCountModifier(DefaultGame game, Filterable filter) {
        return 0;
    }

    @Override
    public boolean hasFlagActive(DefaultGame game, ModifierFlag modifierFlag) {
        return false;
    }

    @Override
    public boolean isSiteReplaceable(DefaultGame game, String playerId) {
        return true;
    }

    @Override
    public boolean canPlaySite(DefaultGame game, String playerId) {
        return true;
    }

    @Override
    public boolean shadowCanHaveInitiative(DefaultGame game) {
        return true;
    }

    @Override
    public Side hasInitiative(DefaultGame game) {
        return null;
    }

    @Override
    public int getInitiativeHandSizeModifier(DefaultGame game) {
        return 0;
    }

    @Override
    public boolean lostAllKeywords(DefaultGame game, LotroPhysicalCard card) {
        return false;
    }

    @Override
    public Evaluator getFpSkirmishStrengthOverrideEvaluator(DefaultGame game, LotroPhysicalCard fpCharacter) {
        return null;
    }

    @Override
    public Evaluator getShadowSkirmishStrengthOverrideEvaluator(DefaultGame game, LotroPhysicalCard shadowCharacter) { return null; }

    @Override
    public boolean canSpotCulture(DefaultGame game, Culture culture, String playerId) {
        return true;
    }

    @Override
    public int getFPCulturesSpotCountModifier(DefaultGame game, String playerId) {
        return 0;
    }

    @Override
    public int getSanctuaryHealModifier(DefaultGame game) {
        return 0;
    }

    @Override
    public int getPotentialDiscount(DefaultGame game, LotroPhysicalCard discountCard) {
        return 0;
    }

    @Override
    public void appendPotentialDiscounts(DefaultGame game, CostToEffectAction action, LotroPhysicalCard card) {

    }
}
