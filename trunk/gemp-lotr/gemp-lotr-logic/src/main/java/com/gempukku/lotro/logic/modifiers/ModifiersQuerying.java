package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

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

    public Collection<Modifier> getModifiersAffecting(GameState gameState, PhysicalCard card);

    public boolean hasTextRemoved(GameState gameState, PhysicalCard card);

    // Keywords
    public boolean hasKeyword(GameState gameState, PhysicalCard physicalCard, Keyword keyword);

    public int getKeywordCount(GameState gameState, PhysicalCard physicalCard, Keyword keyword);

    public boolean hasSignet(GameState gameState, PhysicalCard physicalCard, Signet signet);

    // Archery
    public int getArcheryTotal(GameState gameState, Side side, int baseArcheryTotal);

    public boolean addsToArcheryTotal(GameState gameState, PhysicalCard card);

    // Move limit
    public int getMoveLimit(GameState gameState, int baseMoveLimit);

    // Twilight cost
    public int getTwilightCost(GameState gameState, PhysicalCard physicalCard, boolean ignoreRoamingPenalty);

    public int getPlayOnTwilightCost(GameState gameState, PhysicalCard physicalCard, PhysicalCard target);

    public int getRoamingPenalty(GameState gameState, PhysicalCard physicalCard);

    // Stats
    public int getStrength(GameState gameState, PhysicalCard physicalCard);

    public int getVitality(GameState gameState, PhysicalCard physicalCard);

    public int getResistance(GameState gameState, PhysicalCard physicalCard);

    public int getMinionSiteNumber(GameState gameState, PhysicalCard physicalCard);

    public int getOverwhelmMultiplier(GameState gameState, PhysicalCard card);

    public boolean isAdditionalCardType(GameState gameState, PhysicalCard card, CardType cardType);

    // Wounds/exertions
    public boolean canTakeWounds(GameState gameState, PhysicalCard card, int woundsToTake);

    public boolean canTakeWoundsFromLosingSkirmish(GameState gameState, PhysicalCard card, Set<PhysicalCard> winners);

    public boolean canTakeArcheryWound(GameState gameState, PhysicalCard card);

    public boolean canBeExerted(GameState gameState, PhysicalCard exertionSource, PhysicalCard card);

    public boolean canBeHealed(GameState gameState, PhysicalCard card);

    public boolean canRemoveBurden(GameState gameState, PhysicalCard source);

    public boolean canRemoveThreat(GameState gameState, PhysicalCard source);

    // Assignments
    public boolean canBeAssignedToSkirmish(GameState gameState, Side playerSide, PhysicalCard card);

    public boolean isUnhastyCompanionAllowedToParticipateInSkirmishes(GameState gameState, PhysicalCard card);

    public boolean isAllyAllowedToParticipateInArcheryFire(GameState gameState, PhysicalCard card);

    public boolean isAllyAllowedToParticipateInSkirmishes(GameState gameState, Side sidePlayer, PhysicalCard card);

    public boolean isAllyPreventedFromParticipatingInSkirmishes(GameState gameState, Side sidePlayer, PhysicalCard card);

    public boolean isAllyPreventedFromParticipatingInArcheryFire(GameState gameState, PhysicalCard card);

    public boolean isValidAssignments(GameState gameState, Side side, Map<PhysicalCard, Set<PhysicalCard>> assignments);

    // Playing actions
    public boolean canPlayAction(GameState gameState, String performingPlayer, Action action);

    public boolean canHavePlayedOn(GameState gameState, PhysicalCard playedCard, PhysicalCard target);

    public boolean canHaveTransferredOn(GameState gameState, PhysicalCard playedCard, PhysicalCard target);

    public boolean canBeTransferred(GameState gameState, PhysicalCard attachment);

    public boolean shouldSkipPhase(GameState gameState, Phase phase, String playerId);

    public List<? extends ActivateCardAction> getExtraPhaseActions(GameState gameState, PhysicalCard target);

    public List<? extends Action> getExtraPhaseActionsFromStacked(GameState gameState, PhysicalCard target);

    public boolean canPayExtraCostsToPlay(GameState gameState, PhysicalCard target);

    public List<? extends Effect> getExtraCostsToPlay(GameState gameState, Action action, PhysicalCard target);

    // Others
    public boolean canBeDiscardedFromPlay(GameState gameState, String performingPlayer, PhysicalCard card, PhysicalCard source);

    public boolean canBeReturnedToHand(GameState gameState, PhysicalCard card, PhysicalCard source);

    public boolean canDrawCardNoIncrement(GameState gameState, String playerId);

    public boolean canDrawCardAndIncrement(GameState gameState, String playerId);

    public boolean canLookOrRevealCardsInHand(GameState gameState, String playerId);

    public boolean canDiscardCardsFromHand(GameState gameState, String playerId, PhysicalCard source);

    public boolean canDiscardCardsFromTopOfDeck(GameState gameState, String playerId, PhysicalCard source);

    public Side hasInitiative(GameState gameState);

    public int getSpotBonus(GameState gameState, Filterable filter);

    public boolean hasFlagActive(GameState gameState, ModifierFlag modifierFlag);

    public boolean canReplaceSite(GameState gameState, String playerId, PhysicalCard siteToReplace);

    public boolean canPlaySite(GameState gameState, String playerId);
}
