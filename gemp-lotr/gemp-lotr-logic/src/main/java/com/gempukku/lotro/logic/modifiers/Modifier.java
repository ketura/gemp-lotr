package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.timing.Action;

import java.util.List;
import java.util.Map;

public interface Modifier {
    public PhysicalCard getSource();

    public String getText(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self);

    public ModifierEffect[] getModifierEffects();

    public boolean affectsCard(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard);

    public boolean hasKeyword(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, boolean result);

    public int getKeywordCount(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, int result);

    public boolean appliesKeywordModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard modifierSource, Keyword keyword, boolean result);

    public boolean isKeywordRemoved(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword);

    public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result);

    public boolean appliesStrengthModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard modifierSource, boolean result);

    public int getVitality(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result);

    public int getResistance(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result);

    public int getMinionSiteNumber(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result);

    public int getTwilightCost(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result);

    public int getPlayOnTwilightCost(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, PhysicalCard target, int result);

    public int getRoamingPenalty(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result);

    public boolean isOverwhelmedByStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int strength, int opposingStrength, boolean result);

    public boolean canTakeWound(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, boolean result);

    public boolean canBeExerted(GameState gameState, ModifiersLogic modifiersLogic, PhysicalCard source, PhysicalCard card, boolean result);

    public boolean isAllyParticipateInArcheryFire(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card);

    public boolean isAllyParticipateInSkirmishes(GameState gameState, Side sidePlayer, ModifiersQuerying modifiersQuerying, PhysicalCard card);

    public int getArcheryTotal(GameState gameState, ModifiersQuerying modifiersQuerying, Side side, int result);

    public int getMoveLimit(GameState gameState, ModifiersQuerying modifiersQuerying, int result);

    public boolean addsToArcheryTotal(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card, boolean result);

    public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, String performingPlayer, Action action, boolean result);

    public boolean canHavePlayedOn(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard playedCard, PhysicalCard target);

    public boolean canHaveTransferredOn(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard playedCard, PhysicalCard target);

    public boolean shouldSkipPhase(GameState gameState, ModifiersQuerying modifiersQuerying, Phase phase, String playerId, boolean result);

    public boolean isValidAssignments(GameState gameState, Side Side, ModifiersQuerying modifiersQuerying, PhysicalCard companion, List<PhysicalCard> minions, boolean result);

    public boolean isValidAssignments(GameState gameState, Side Side, ModifiersQuerying modifiersQuerying, Map<PhysicalCard, List<PhysicalCard>> assignments, boolean result);

    public boolean canBeAssignedToSkirmish(GameState gameState, Side sidePlayer, ModifiersQuerying modifiersQuerying, PhysicalCard card);

    public boolean canBeDiscardedFromPlay(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card, PhysicalCard source, boolean result);

    public boolean canBeHealed(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card, boolean result);

    public boolean canLookOrRevealCardsInHand(GameState gameState, ModifiersQuerying modifiersQuerying, String playerId);

    public boolean canDiscardCardsFromHand(GameState gameState, ModifiersQuerying modifiersQuerying, String playerId, PhysicalCard source);

    public boolean canDiscardCardsFromTopOfDeck(GameState gameState, ModifiersQuerying modifiersQuerying, String playerId, PhysicalCard source);

    public int getSpotCount(GameState gameState, ModifiersQuerying modifiersQuerying, Filter filter, int result);

    public boolean hasFlagActive(ModifierFlag modifierFlag);
}
