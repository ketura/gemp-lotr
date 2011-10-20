package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;

public class SubAction implements Action {
    private Action _action;
    private LinkedList<Effect> _effects = new LinkedList<Effect>();

    public SubAction(Action action) {
        _action = action;
    }

    public void appendEffect(Effect effect) {
        _effects.add(effect);
    }

    @Override
    public PhysicalCard getActionSource() {
        return _action.getActionSource();
    }

    @Override
    public PhysicalCard getActionAttachedToCard() {
        return _action.getActionAttachedToCard();
    }

    @Override
    public Phase getActionTimeword() {
        return _action.getActionTimeword();
    }

    @Override
    public void setActionTimeword(Phase phase) {
        _action.setActionTimeword(phase);
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Effect nextEffect(LotroGame game) {
        return _effects.poll();
    }
}
