package com.gempukku.lotro.cards.modifiers.conditions;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class InitiativeCondition implements Condition {
    private Side _side;

    public InitiativeCondition(Side side) {
        _side = side;
    }

    @Override
    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return gameState.getInitiativeSide() == _side;
    }
}
