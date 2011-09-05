package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.List;

public interface ModifiersQuerying {
    public Collection<Modifier> getModifiersAffecting(GameState gameState, PhysicalCard card);

    public boolean hasKeyword(GameState gameState, PhysicalCard physicalCard, Keyword keyword);

    public int getKeywordCount(GameState gameState, PhysicalCard physicalCard, Keyword keyword);

    public int getArcheryTotal(GameState gameState, Side side, int baseArcheryTotal);

    public int getMoveLimit(GameState gameState, int baseMoveLimit);

    public int getTwilightCost(GameState gameState, PhysicalCard physicalCard);

    public int getPlayOnTwilightCost(GameState gameState, PhysicalCard physicalCard, PhysicalCard target);

    public int getStrength(GameState gameState, PhysicalCard physicalCard);

    public int getVitality(GameState gameState, PhysicalCard physicalCard);

    public boolean isOverwhelmedByStrength(GameState gameState, PhysicalCard card, int strength, int opposingStrength);

    public boolean canTakeWound(GameState gameState, PhysicalCard card);

    public boolean isAllyOnCurrentSite(GameState gameState, PhysicalCard card);

    public boolean addsToArcheryTotal(GameState gameState, PhysicalCard card);

    public boolean canPlayAction(GameState gameState, Action action);

    public boolean shouldSkipPhase(GameState gameState, Phase phase);

    public boolean isValidFreePlayerAssignments(GameState gameState, PhysicalCard companion, List<PhysicalCard> minions);
}
