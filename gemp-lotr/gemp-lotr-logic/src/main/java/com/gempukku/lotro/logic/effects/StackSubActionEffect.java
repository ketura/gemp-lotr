package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.timing.Effect;

public class StackSubActionEffect implements Effect {
    private SubAction subAction;

    public StackSubActionEffect(SubAction subAction) {
        this.subAction = subAction;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }

    @Override
    public void playEffect(LotroGame game) {
        game.getActionsEnvironment().addActionToStack(subAction);
    }

    @Override
    public boolean wasCarriedOut() {
        return subAction.wasCarriedOut();
    }

    @Override
    public PhysicalCard getSource() {
        return subAction.getActionSource();
    }

    @Override
    public String getPerformingPlayer() {
        return subAction.getPerformingPlayer();
    }
}
