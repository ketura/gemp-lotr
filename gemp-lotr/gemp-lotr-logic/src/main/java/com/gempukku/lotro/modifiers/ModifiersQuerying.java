package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.actions.Action;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ModifiersQuerying {
    public LimitCounter getUntilEndOfPhaseLimitCounter(LotroPhysicalCard card, Phase phase);

    public LimitCounter getUntilEndOfPhaseLimitCounter(LotroPhysicalCard card, String prefix, Phase phase);

    public LimitCounter getUntilStartOfPhaseLimitCounter(LotroPhysicalCard card, Phase phase);

    public LimitCounter getUntilStartOfPhaseLimitCounter(LotroPhysicalCard card, String prefix, Phase phase);

    public LimitCounter getUntilEndOfTurnLimitCounter(LotroPhysicalCard card);

    public LimitCounter getUntilEndOfTurnLimitCounter(LotroPhysicalCard card, String prefix);

    public Collection<Modifier> getModifiersAffecting(DefaultGame game, LotroPhysicalCard card);

    public Evaluator getFPStrengthOverrideEvaluator(DefaultGame game, LotroPhysicalCard fpCharacter);
    public Evaluator getShadowStrengthOverrideEvaluator(DefaultGame game, LotroPhysicalCard fpCharacter);

    public boolean hasTextRemoved(DefaultGame game, LotroPhysicalCard card);

    // Keywords
    public boolean hasKeyword(DefaultGame game, LotroPhysicalCard physicalCard, Keyword keyword);

    public int getKeywordCount(DefaultGame game, LotroPhysicalCard physicalCard, Keyword keyword);

    public boolean hasSignet(DefaultGame game, LotroPhysicalCard physicalCard, Signet signet);

    // Archery
    public int getArcheryTotal(DefaultGame game, Side side, int baseArcheryTotal);

    public boolean addsToArcheryTotal(DefaultGame game, LotroPhysicalCard card);

    // Movement
    public int getMoveLimit(DefaultGame game, int baseMoveLimit);

    public boolean addsTwilightForCompanionMove(DefaultGame game, LotroPhysicalCard companion);

    // Twilight cost
    public int getTwilightCost(DefaultGame game, LotroPhysicalCard physicalCard, LotroPhysicalCard target, int twilightCostModifier, boolean ignoreRoamingPenalty);

    public int getRoamingPenalty(DefaultGame game, LotroPhysicalCard physicalCard);

    // Stats
    public int getStrength(DefaultGame game, LotroPhysicalCard physicalCard);

    public boolean appliesStrengthBonusModifier(DefaultGame game, LotroPhysicalCard modifierSource, LotroPhysicalCard modifierTarget);

    public int getVitality(DefaultGame game, LotroPhysicalCard physicalCard);

    public int getResistance(DefaultGame game, LotroPhysicalCard physicalCard);

    public int getMinionSiteNumber(DefaultGame game, LotroPhysicalCard physicalCard);

    public int getOverwhelmMultiplier(DefaultGame game, LotroPhysicalCard card);

    public boolean isAdditionalCardType(DefaultGame game, LotroPhysicalCard card, CardType cardType);

    // Wounds/exertions
    public boolean canTakeWounds(DefaultGame game, Collection<LotroPhysicalCard> woundSources, LotroPhysicalCard card, int woundsToTake);

    public boolean canTakeWoundsFromLosingSkirmish(DefaultGame game, LotroPhysicalCard card, Set<LotroPhysicalCard> winners);

    public boolean canTakeArcheryWound(DefaultGame game, LotroPhysicalCard card);

    public boolean canBeExerted(DefaultGame game, LotroPhysicalCard exertionSource, LotroPhysicalCard exertedCard);

    public boolean canBeHealed(DefaultGame game, LotroPhysicalCard card);

    public boolean canAddBurden(DefaultGame game, String performingPlayer, LotroPhysicalCard source);

    public boolean canRemoveBurden(DefaultGame game, LotroPhysicalCard source);

    public boolean canRemoveThreat(DefaultGame game, LotroPhysicalCard source);

    // Assignments
    public boolean canBeAssignedToSkirmish(DefaultGame game, Side playerSide, LotroPhysicalCard card);

    boolean canCancelSkirmish(DefaultGame game, LotroPhysicalCard card);

    public boolean isUnhastyCompanionAllowedToParticipateInSkirmishes(DefaultGame game, LotroPhysicalCard card);

    public boolean isAllyAllowedToParticipateInArcheryFire(DefaultGame game, LotroPhysicalCard card);

    public boolean isAllyAllowedToParticipateInSkirmishes(DefaultGame game, Side sidePlayer, LotroPhysicalCard card);

    public boolean isAllyPreventedFromParticipatingInSkirmishes(DefaultGame game, Side sidePlayer, LotroPhysicalCard card);

    public boolean isAllyPreventedFromParticipatingInArcheryFire(DefaultGame game, LotroPhysicalCard card);

    public boolean isValidAssignments(DefaultGame game, Side side, Map<LotroPhysicalCard, Set<LotroPhysicalCard>> assignments);

    // Playing actions
    public boolean canPlayAction(DefaultGame game, String performingPlayer, Action action);

    public boolean canPlayCard(DefaultGame game, String performingPlayer, LotroPhysicalCard card);

    public boolean canHavePlayedOn(DefaultGame game, LotroPhysicalCard playedCard, LotroPhysicalCard target);

    public boolean canHaveTransferredOn(DefaultGame game, LotroPhysicalCard playedCard, LotroPhysicalCard target);

    public boolean canBeTransferred(DefaultGame game, LotroPhysicalCard attachment);

    public boolean shouldSkipPhase(DefaultGame game, Phase phase, String playerId);

    public List<? extends Action> getExtraPhaseActions(DefaultGame game, LotroPhysicalCard target);

    public List<? extends Action> getExtraPhaseActionsFromStacked(DefaultGame game, LotroPhysicalCard target);

    public boolean canPayExtraCostsToPlay(DefaultGame game, LotroPhysicalCard target);

    public void appendExtraCosts(DefaultGame game, CostToEffectAction action, LotroPhysicalCard target);

    // Others
    public boolean canBeDiscardedFromPlay(DefaultGame game, String performingPlayer, LotroPhysicalCard card, LotroPhysicalCard source);

    public boolean canBeReturnedToHand(DefaultGame game, LotroPhysicalCard card, LotroPhysicalCard source);

    public boolean canDrawCardNoIncrement(DefaultGame game, String playerId);

    public boolean canDrawCardAndIncrementForRuleOfFour(DefaultGame game, String playerId);

    public boolean canLookOrRevealCardsInHand(DefaultGame game, String revealingPlayerId, String performingPlayerId);

    public boolean canDiscardCardsFromHand(DefaultGame game, String playerId, LotroPhysicalCard source);

    public boolean canDiscardCardsFromTopOfDeck(DefaultGame game, String playerId, LotroPhysicalCard source);

    public boolean canBeLiberated(DefaultGame game, String playerId, LotroPhysicalCard card, LotroPhysicalCard source);

    public Side hasInitiative(DefaultGame game);

    public int getNumberOfSpottableFPCultures(DefaultGame game, String playerId);

    public int getNumberOfSpottableShadowCultures(DefaultGame game, String playerId);

    public int getSpotBonus(DefaultGame game, Filterable filter);

    public boolean hasFlagActive(DefaultGame game, ModifierFlag modifierFlag);

    public boolean canReplaceSite(DefaultGame game, String playerId, LotroPhysicalCard siteToReplace);

    public boolean canPlaySite(DefaultGame game, String playerId);

    int getSanctuaryHealModifier(DefaultGame game);

    int getPotentialDiscount(DefaultGame game, LotroPhysicalCard playedCard);

    void appendPotentialDiscounts(DefaultGame game, CostToEffectAction action, LotroPhysicalCard playedCard);
}
