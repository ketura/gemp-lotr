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

public interface ModifiersQuerying {
    public LimitCounter getUntilEndOfPhaseLimitCounter(PhysicalCard card, Phase phase);

    public LimitCounter getUntilEndOfPhaseLimitCounter(PhysicalCard card, String prefix, Phase phase);

    public LimitCounter getUntilStartOfPhaseLimitCounter(PhysicalCard card, Phase phase);

    public LimitCounter getUntilStartOfPhaseLimitCounter(PhysicalCard card, String prefix, Phase phase);

    public LimitCounter getUntilEndOfTurnLimitCounter(PhysicalCard card);

    public LimitCounter getUntilEndOfTurnLimitCounter(PhysicalCard card, String prefix);

    public Collection<Modifier> getModifiersAffecting(DefaultGame game, PhysicalCard card);

    public Evaluator getFPStrengthOverrideEvaluator(DefaultGame game, PhysicalCard fpCharacter);
    public Evaluator getShadowStrengthOverrideEvaluator(DefaultGame game, PhysicalCard fpCharacter);

    public boolean hasTextRemoved(DefaultGame game, PhysicalCard card);

    // Keywords
    public boolean hasKeyword(DefaultGame game, PhysicalCard physicalCard, Keyword keyword);

    public int getKeywordCount(DefaultGame game, PhysicalCard physicalCard, Keyword keyword);

    public boolean hasSignet(DefaultGame game, PhysicalCard physicalCard, Signet signet);

    // Archery
    public int getArcheryTotal(DefaultGame game, Side side, int baseArcheryTotal);

    public boolean addsToArcheryTotal(DefaultGame game, PhysicalCard card);

    // Movement
    public int getMoveLimit(DefaultGame game, int baseMoveLimit);

    public boolean addsTwilightForCompanionMove(DefaultGame game, PhysicalCard companion);

    // Twilight cost
    public int getTwilightCost(DefaultGame game, PhysicalCard physicalCard, PhysicalCard target, int twilightCostModifier, boolean ignoreRoamingPenalty);

    public int getRoamingPenalty(DefaultGame game, PhysicalCard physicalCard);

    // Stats
    public int getStrength(DefaultGame game, PhysicalCard physicalCard);

    public boolean appliesStrengthBonusModifier(DefaultGame game, PhysicalCard modifierSource, PhysicalCard modifierTarget);

    public int getVitality(DefaultGame game, PhysicalCard physicalCard);

    public int getResistance(DefaultGame game, PhysicalCard physicalCard);

    public int getMinionSiteNumber(DefaultGame game, PhysicalCard physicalCard);

    public int getOverwhelmMultiplier(DefaultGame game, PhysicalCard card);

    public boolean isAdditionalCardType(DefaultGame game, PhysicalCard card, CardType cardType);

    // Wounds/exertions
    public boolean canTakeWounds(DefaultGame game, Collection<PhysicalCard> woundSources, PhysicalCard card, int woundsToTake);

    public boolean canTakeWoundsFromLosingSkirmish(DefaultGame game, PhysicalCard card, Set<PhysicalCard> winners);

    public boolean canTakeArcheryWound(DefaultGame game, PhysicalCard card);

    public boolean canBeExerted(DefaultGame game, PhysicalCard exertionSource, PhysicalCard exertedCard);

    public boolean canBeHealed(DefaultGame game, PhysicalCard card);

    public boolean canAddBurden(DefaultGame game, String performingPlayer, PhysicalCard source);

    public boolean canRemoveBurden(DefaultGame game, PhysicalCard source);

    public boolean canRemoveThreat(DefaultGame game, PhysicalCard source);

    // Assignments
    public boolean canBeAssignedToSkirmish(DefaultGame game, Side playerSide, PhysicalCard card);

    boolean canCancelSkirmish(DefaultGame game, PhysicalCard card);

    public boolean isUnhastyCompanionAllowedToParticipateInSkirmishes(DefaultGame game, PhysicalCard card);

    public boolean isAllyAllowedToParticipateInArcheryFire(DefaultGame game, PhysicalCard card);

    public boolean isAllyAllowedToParticipateInSkirmishes(DefaultGame game, Side sidePlayer, PhysicalCard card);

    public boolean isAllyPreventedFromParticipatingInSkirmishes(DefaultGame game, Side sidePlayer, PhysicalCard card);

    public boolean isAllyPreventedFromParticipatingInArcheryFire(DefaultGame game, PhysicalCard card);

    public boolean isValidAssignments(DefaultGame game, Side side, Map<PhysicalCard, Set<PhysicalCard>> assignments);

    // Playing actions
    public boolean canPlayAction(DefaultGame game, String performingPlayer, Action action);

    public boolean canPlayCard(DefaultGame game, String performingPlayer, PhysicalCard card);

    public boolean canHavePlayedOn(DefaultGame game, PhysicalCard playedCard, PhysicalCard target);

    public boolean canHaveTransferredOn(DefaultGame game, PhysicalCard playedCard, PhysicalCard target);

    public boolean canBeTransferred(DefaultGame game, PhysicalCard attachment);

    public boolean shouldSkipPhase(DefaultGame game, Phase phase, String playerId);

    public List<? extends Action> getExtraPhaseActions(DefaultGame game, PhysicalCard target);

    public List<? extends Action> getExtraPhaseActionsFromStacked(DefaultGame game, PhysicalCard target);

    public boolean canPayExtraCostsToPlay(DefaultGame game, PhysicalCard target);

    public void appendExtraCosts(DefaultGame game, CostToEffectAction action, PhysicalCard target);

    // Others
    public boolean canBeDiscardedFromPlay(DefaultGame game, String performingPlayer, PhysicalCard card, PhysicalCard source);

    public boolean canBeReturnedToHand(DefaultGame game, PhysicalCard card, PhysicalCard source);

    public boolean canDrawCardNoIncrement(DefaultGame game, String playerId);

    public boolean canDrawCardAndIncrementForRuleOfFour(DefaultGame game, String playerId);

    public boolean canLookOrRevealCardsInHand(DefaultGame game, String revealingPlayerId, String performingPlayerId);

    public boolean canDiscardCardsFromHand(DefaultGame game, String playerId, PhysicalCard source);

    public boolean canDiscardCardsFromTopOfDeck(DefaultGame game, String playerId, PhysicalCard source);

    public boolean canBeLiberated(DefaultGame game, String playerId, PhysicalCard card, PhysicalCard source);

    public Side hasInitiative(DefaultGame game);

    public int getNumberOfSpottableFPCultures(DefaultGame game, String playerId);

    public int getNumberOfSpottableShadowCultures(DefaultGame game, String playerId);

    public int getSpotBonus(DefaultGame game, Filterable filter);

    public boolean hasFlagActive(DefaultGame game, ModifierFlag modifierFlag);

    public boolean canReplaceSite(DefaultGame game, String playerId, PhysicalCard siteToReplace);

    public boolean canPlaySite(DefaultGame game, String playerId);

    int getSanctuaryHealModifier(DefaultGame game);

    int getPotentialDiscount(DefaultGame game, PhysicalCard playedCard);

    void appendPotentialDiscounts(DefaultGame game, CostToEffectAction action, PhysicalCard playedCard);
}
