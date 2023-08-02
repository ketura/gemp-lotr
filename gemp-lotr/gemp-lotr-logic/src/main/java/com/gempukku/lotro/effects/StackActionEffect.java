package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.lotronly.CostToEffectAction;

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
    public LotroPhysicalCard getSource() {
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
