package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.CostToEffectAction;
import com.gempukku.lotro.game.timing.Effect;

public class StackActionEffect implements Effect {
    private final CostToEffectAction _action;

    public StackActionEffect(CostToEffectAction action) {
        _action = action;
    }

    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return true;
    }

    @Override
    public void playEffect(DefaultGame game) {
        game.getActionsEnvironment().addActionToStack(_action);
    }

    @Override
    public PhysicalCard getSource() {
        return _action.getActionSource();
    }

    @Override
    public String getPerformingPlayer() {
        return _action.getPerformingPlayer();
    }

    @Override
    public boolean wasCarriedOut() {
        return _action.wasCarriedOut();
    }
}
