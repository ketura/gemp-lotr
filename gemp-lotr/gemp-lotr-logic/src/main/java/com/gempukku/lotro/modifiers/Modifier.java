package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.condition.Condition;
import com.gempukku.lotro.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.actions.Action;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Modifier {
    LotroPhysicalCard getSource();

    String getText(DefaultGame game, LotroPhysicalCard self);

    ModifierEffect getModifierEffect();

    boolean isNonCardTextModifier();

    Condition getCondition();

    boolean isExtraPossessionClass(DefaultGame game, LotroPhysicalCard physicalCard, LotroPhysicalCard attachedTo);

    boolean affectsCard(DefaultGame game, LotroPhysicalCard physicalCard);

    boolean hasRemovedText(DefaultGame game, LotroPhysicalCard physicalCard);

    boolean hasKeyword(DefaultGame game, LotroPhysicalCard physicalCard, Keyword keyword);

    boolean hasSignet(DefaultGame game, LotroPhysicalCard physicalCard, Signet signet);

    int getKeywordCountModifier(DefaultGame game, LotroPhysicalCard physicalCard, Keyword keyword);

    boolean appliesKeywordModifier(DefaultGame game, LotroPhysicalCard modifierSource, Keyword keyword);

    boolean isKeywordRemoved(DefaultGame game, LotroPhysicalCard physicalCard, Keyword keyword);

    int getStrengthModifier(DefaultGame game, LotroPhysicalCard physicalCard);

    boolean appliesStrengthBonusModifier(DefaultGame game, LotroPhysicalCard modifierSource, LotroPhysicalCard modifierTaget);

    int getVitalityModifier(DefaultGame game, LotroPhysicalCard physicalCard);

    int getResistanceModifier(DefaultGame game, LotroPhysicalCard physicalCard);

    int getMinionSiteNumberModifier(DefaultGame game, LotroPhysicalCard physicalCard);

    boolean isAdditionalCardTypeModifier(DefaultGame game, LotroPhysicalCard physicalCard, CardType cardType);

    int getTwilightCostModifier(DefaultGame game, LotroPhysicalCard physicalCard, LotroPhysicalCard target, boolean ignoreRoamingPenalty);

    int getRoamingPenaltyModifier(DefaultGame game, LotroPhysicalCard physicalCard);

    int getOverwhelmMultiplier(DefaultGame game, LotroPhysicalCard physicalCard);

    boolean canCancelSkirmish(DefaultGame game, LotroPhysicalCard physicalCard);

    boolean canTakeWounds(DefaultGame game, Collection<LotroPhysicalCard> woundSources, LotroPhysicalCard physicalCard, int woundsAlreadyTakenInPhase, int woundsToTake);

    boolean canTakeWoundsFromLosingSkirmish(DefaultGame game, LotroPhysicalCard physicalCard, Set<LotroPhysicalCard> winners);

    boolean canTakeArcheryWound(DefaultGame game, LotroPhysicalCard physicalCard);

    boolean canBeExerted(DefaultGame game, LotroPhysicalCard exertionSource, LotroPhysicalCard exertedCard);

    boolean isAllyParticipateInArcheryFire(DefaultGame game, LotroPhysicalCard card);

    boolean isAllyParticipateInSkirmishes(DefaultGame game, Side sidePlayer, LotroPhysicalCard card);

    boolean isUnhastyCompanionAllowedToParticipateInSkirmishes(DefaultGame game, LotroPhysicalCard card);

    boolean isAllyPreventedFromParticipatingInArcheryFire(DefaultGame game, LotroPhysicalCard card);

    boolean isAllyPreventedFromParticipatingInSkirmishes(DefaultGame game, Side sidePlayer, LotroPhysicalCard card);

    int getArcheryTotalModifier(DefaultGame game, Side side);

    int getMoveLimitModifier(DefaultGame game);

    boolean addsTwilightForCompanionMove(DefaultGame game, LotroPhysicalCard companion);

    boolean addsToArcheryTotal(DefaultGame game, LotroPhysicalCard card);

    boolean canPlayAction(DefaultGame game, String performingPlayer, Action action);

    boolean canPlayCard(DefaultGame game, String performingPlayer, LotroPhysicalCard card);

    List<? extends Action> getExtraPhaseAction(DefaultGame game, LotroPhysicalCard card);

    List<? extends Action> getExtraPhaseActionFromStacked(DefaultGame game, LotroPhysicalCard card);

    boolean canPayExtraCostsToPlay(DefaultGame game, LotroPhysicalCard card);

    void appendExtraCosts(DefaultGame game, CostToEffectAction action, LotroPhysicalCard card);

    boolean canHavePlayedOn(DefaultGame game, LotroPhysicalCard playedCard, LotroPhysicalCard target);

    boolean canHaveTransferredOn(DefaultGame game, LotroPhysicalCard playedCard, LotroPhysicalCard target);

    boolean canBeTransferred(DefaultGame game, LotroPhysicalCard attachment);

    boolean shouldSkipPhase(DefaultGame game, Phase phase, String playerId);

    boolean isValidAssignments(DefaultGame game, Side Side, LotroPhysicalCard companion, Set<LotroPhysicalCard> minions);

    boolean isValidAssignments(DefaultGame game, Side Side, Map<LotroPhysicalCard, Set<LotroPhysicalCard>> assignments);

    boolean isPreventedFromBeingAssignedToSkirmish(DefaultGame game, Side sidePlayer, LotroPhysicalCard card);

    boolean canBeDiscardedFromPlay(DefaultGame game, String performingPlayer, LotroPhysicalCard card, LotroPhysicalCard source);

    boolean canBeLiberated(DefaultGame game, String performingPlayer, LotroPhysicalCard card, LotroPhysicalCard source);

    boolean canBeReturnedToHand(DefaultGame game, LotroPhysicalCard card, LotroPhysicalCard source);

    boolean canBeHealed(DefaultGame game, LotroPhysicalCard card);

    boolean canAddBurden(DefaultGame game, String performingPlayer, LotroPhysicalCard source);

    boolean canRemoveBurden(DefaultGame game, LotroPhysicalCard source);

    boolean canRemoveThreat(DefaultGame game, LotroPhysicalCard source);

    boolean canLookOrRevealCardsInHand(DefaultGame game, String revealingPlayerId, String actingPlayerId);

    boolean canDiscardCardsFromHand(DefaultGame game, String playerId, LotroPhysicalCard source);

    boolean canDiscardCardsFromTopOfDeck(DefaultGame game, String playerId, LotroPhysicalCard source);

    int getSpotCountModifier(DefaultGame game, Filterable filter);

    boolean hasFlagActive(DefaultGame game, ModifierFlag modifierFlag);

    boolean isSiteReplaceable(DefaultGame game, String playerId);

    boolean canPlaySite(DefaultGame game, String playerId);

    boolean shadowCanHaveInitiative(DefaultGame game);

    Side hasInitiative(DefaultGame game);

    int getInitiativeHandSizeModifier(DefaultGame game);

    boolean lostAllKeywords(DefaultGame game, LotroPhysicalCard card);

    Evaluator getFpSkirmishStrengthOverrideEvaluator(DefaultGame game, LotroPhysicalCard fpCharacter);
    Evaluator getShadowSkirmishStrengthOverrideEvaluator(DefaultGame game, LotroPhysicalCard fpCharacter);

    boolean canSpotCulture(DefaultGame game, Culture culture, String playerId);

    int getFPCulturesSpotCountModifier(DefaultGame game, String playerId);

    int getSanctuaryHealModifier(DefaultGame game);

    int getPotentialDiscount(DefaultGame game, LotroPhysicalCard discountCard);

    void appendPotentialDiscounts(DefaultGame game, CostToEffectAction action, LotroPhysicalCard card);
}
