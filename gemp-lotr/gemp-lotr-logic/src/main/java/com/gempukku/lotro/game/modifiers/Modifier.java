package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.game.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.game.actions.Action;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Modifier {
    PhysicalCard getSource();

    String getText(DefaultGame game, PhysicalCard self);

    ModifierEffect getModifierEffect();

    boolean isNonCardTextModifier();

    Condition getCondition();

    boolean isExtraPossessionClass(DefaultGame game, PhysicalCard physicalCard, PhysicalCard attachedTo);

    boolean affectsCard(DefaultGame game, PhysicalCard physicalCard);

    boolean hasRemovedText(DefaultGame game, PhysicalCard physicalCard);

    boolean hasKeyword(DefaultGame game, PhysicalCard physicalCard, Keyword keyword);

    boolean hasSignet(DefaultGame game, PhysicalCard physicalCard, Signet signet);

    int getKeywordCountModifier(DefaultGame game, PhysicalCard physicalCard, Keyword keyword);

    boolean appliesKeywordModifier(DefaultGame game, PhysicalCard modifierSource, Keyword keyword);

    boolean isKeywordRemoved(DefaultGame game, PhysicalCard physicalCard, Keyword keyword);

    int getStrengthModifier(DefaultGame game, PhysicalCard physicalCard);

    boolean appliesStrengthBonusModifier(DefaultGame game, PhysicalCard modifierSource, PhysicalCard modifierTaget);

    int getVitalityModifier(DefaultGame game, PhysicalCard physicalCard);

    int getResistanceModifier(DefaultGame game, PhysicalCard physicalCard);

    int getMinionSiteNumberModifier(DefaultGame game, PhysicalCard physicalCard);

    boolean isAdditionalCardTypeModifier(DefaultGame game, PhysicalCard physicalCard, CardType cardType);

    int getTwilightCostModifier(DefaultGame game, PhysicalCard physicalCard, PhysicalCard target, boolean ignoreRoamingPenalty);

    int getRoamingPenaltyModifier(DefaultGame game, PhysicalCard physicalCard);

    int getOverwhelmMultiplier(DefaultGame game, PhysicalCard physicalCard);

    boolean canCancelSkirmish(DefaultGame game, PhysicalCard physicalCard);

    boolean canTakeWounds(DefaultGame game, Collection<PhysicalCard> woundSources, PhysicalCard physicalCard, int woundsAlreadyTakenInPhase, int woundsToTake);

    boolean canTakeWoundsFromLosingSkirmish(DefaultGame game, PhysicalCard physicalCard, Set<PhysicalCard> winners);

    boolean canTakeArcheryWound(DefaultGame game, PhysicalCard physicalCard);

    boolean canBeExerted(DefaultGame game, PhysicalCard exertionSource, PhysicalCard exertedCard);

    boolean isAllyParticipateInArcheryFire(DefaultGame game, PhysicalCard card);

    boolean isAllyParticipateInSkirmishes(DefaultGame game, Side sidePlayer, PhysicalCard card);

    boolean isUnhastyCompanionAllowedToParticipateInSkirmishes(DefaultGame game, PhysicalCard card);

    boolean isAllyPreventedFromParticipatingInArcheryFire(DefaultGame game, PhysicalCard card);

    boolean isAllyPreventedFromParticipatingInSkirmishes(DefaultGame game, Side sidePlayer, PhysicalCard card);

    int getArcheryTotalModifier(DefaultGame game, Side side);

    int getMoveLimitModifier(DefaultGame game);

    boolean addsTwilightForCompanionMove(DefaultGame game, PhysicalCard companion);

    boolean addsToArcheryTotal(DefaultGame game, PhysicalCard card);

    boolean canPlayAction(DefaultGame game, String performingPlayer, Action action);

    boolean canPlayCard(DefaultGame game, String performingPlayer, PhysicalCard card);

    List<? extends Action> getExtraPhaseAction(DefaultGame game, PhysicalCard card);

    List<? extends Action> getExtraPhaseActionFromStacked(DefaultGame game, PhysicalCard card);

    boolean canPayExtraCostsToPlay(DefaultGame game, PhysicalCard card);

    void appendExtraCosts(DefaultGame game, CostToEffectAction action, PhysicalCard card);

    boolean canHavePlayedOn(DefaultGame game, PhysicalCard playedCard, PhysicalCard target);

    boolean canHaveTransferredOn(DefaultGame game, PhysicalCard playedCard, PhysicalCard target);

    boolean canBeTransferred(DefaultGame game, PhysicalCard attachment);

    boolean shouldSkipPhase(DefaultGame game, Phase phase, String playerId);

    boolean isValidAssignments(DefaultGame game, Side Side, PhysicalCard companion, Set<PhysicalCard> minions);

    boolean isValidAssignments(DefaultGame game, Side Side, Map<PhysicalCard, Set<PhysicalCard>> assignments);

    boolean isPreventedFromBeingAssignedToSkirmish(DefaultGame game, Side sidePlayer, PhysicalCard card);

    boolean canBeDiscardedFromPlay(DefaultGame game, String performingPlayer, PhysicalCard card, PhysicalCard source);

    boolean canBeLiberated(DefaultGame game, String performingPlayer, PhysicalCard card, PhysicalCard source);

    boolean canBeReturnedToHand(DefaultGame game, PhysicalCard card, PhysicalCard source);

    boolean canBeHealed(DefaultGame game, PhysicalCard card);

    boolean canAddBurden(DefaultGame game, String performingPlayer, PhysicalCard source);

    boolean canRemoveBurden(DefaultGame game, PhysicalCard source);

    boolean canRemoveThreat(DefaultGame game, PhysicalCard source);

    boolean canLookOrRevealCardsInHand(DefaultGame game, String revealingPlayerId, String actingPlayerId);

    boolean canDiscardCardsFromHand(DefaultGame game, String playerId, PhysicalCard source);

    boolean canDiscardCardsFromTopOfDeck(DefaultGame game, String playerId, PhysicalCard source);

    int getSpotCountModifier(DefaultGame game, Filterable filter);

    boolean hasFlagActive(DefaultGame game, ModifierFlag modifierFlag);

    boolean isSiteReplaceable(DefaultGame game, String playerId);

    boolean canPlaySite(DefaultGame game, String playerId);

    boolean shadowCanHaveInitiative(DefaultGame game);

    Side hasInitiative(DefaultGame game);

    int getInitiativeHandSizeModifier(DefaultGame game);

    boolean lostAllKeywords(DefaultGame game, PhysicalCard card);

    Evaluator getFpSkirmishStrengthOverrideEvaluator(DefaultGame game, PhysicalCard fpCharacter);
    Evaluator getShadowSkirmishStrengthOverrideEvaluator(DefaultGame game, PhysicalCard fpCharacter);

    boolean canSpotCulture(DefaultGame game, Culture culture, String playerId);

    int getFPCulturesSpotCountModifier(DefaultGame game, String playerId);

    int getSanctuaryHealModifier(DefaultGame game);

    int getPotentialDiscount(DefaultGame game, PhysicalCard discountCard);

    void appendPotentialDiscounts(DefaultGame game, CostToEffectAction action, PhysicalCard card);
}
