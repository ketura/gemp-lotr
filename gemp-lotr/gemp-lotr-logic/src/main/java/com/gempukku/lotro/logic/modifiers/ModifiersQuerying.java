package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ModifiersQuerying {
    public Collection<Modifier> getModifiersAffecting(GameState gameState, PhysicalCard card);

    // Keywords
    public boolean hasKeyword(GameState gameState, PhysicalCard physicalCard, Keyword keyword);

    public int getKeywordCount(GameState gameState, PhysicalCard physicalCard, Keyword keyword);

    // Archery
    public int getArcheryTotal(GameState gameState, Side side, int baseArcheryTotal);

    public boolean addsToArcheryTotal(GameState gameState, PhysicalCard card);


    public int getMoveLimit(GameState gameState, int baseMoveLimit);

    // Twilight cost
    public int getTwilightCost(GameState gameState, PhysicalCard physicalCard);

    public int getPlayOnTwilightCost(GameState gameState, PhysicalCard physicalCard, PhysicalCard target);

    public int getRoamingPenalty(GameState gameState, PhysicalCard physicalCard);

    // Stats
    public int getStrength(GameState gameState, PhysicalCard physicalCard);

    public int getVitality(GameState gameState, PhysicalCard physicalCard);

    public int getResistance(GameState gameState, PhysicalCard physicalCard);

    public int getMinionSiteNumber(GameState gameState, PhysicalCard physicalCard);

    public boolean isOverwhelmedByStrength(GameState gameState, PhysicalCard card, int strength, int opposingStrength);

    // Wounds/exertions
    public boolean canTakeWound(GameState gameState, PhysicalCard card);

    public boolean canBeExerted(GameState gameState, PhysicalCard exertionSource, PhysicalCard card);

    public boolean canBeHealed(GameState gameState, PhysicalCard card);

    // Assignments
    public boolean canBeAssignedToSkirmish(GameState gameState, Side playerSide, PhysicalCard card);

    public boolean isAllyParticipateInSkirmishes(GameState gameState, Side sidePlayer, PhysicalCard card);

    public boolean isAllyParticipateInArcheryFire(GameState gameState, PhysicalCard card);

    public boolean isValidAssignments(GameState gameState, Side side, Map<PhysicalCard, List<PhysicalCard>> assignments);

    // Playing actions
    public boolean canPlayAction(GameState gameState, Action action);

    public boolean canHavePlayedOn(GameState gameState, PhysicalCard playedCard, PhysicalCard target);

    public boolean canHaveTransferredOn(GameState gameState, PhysicalCard playedCard, PhysicalCard target);

    public boolean shouldSkipPhase(GameState gameState, Phase phase, String playerId);

    // Others
    public boolean canBeDiscardedFromPlay(GameState gameState, PhysicalCard card, PhysicalCard source);

    public boolean canDrawCardAndIncrement(GameState gameState, String playerId);

    public boolean canLookOrRevealCardsInHand(GameState gameState, String playerId);

    public int getSpotCount(GameState gameState, Filter filter, int inPlayCount);

    public boolean hasToMoveIfPossible();
}
