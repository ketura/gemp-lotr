package com.gempukku.lotro.cards.actions;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.AbstractCostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

public class SubCostToEffectAction extends AbstractCostToEffectAction {
    private Action _action;

    public SubCostToEffectAction(Action action) {
        _action = action;
    }

    @Override
    public PhysicalCard getActionAttachedToCard() {
        return _action.getActionAttachedToCard();
    }

    @Override
    public PhysicalCard getActionSource() {
        return _action.getActionSource();
    }

    @Override
    public Phase getActionTimeword() {
        return _action.getActionTimeword();
    }

    @Override
    public String getPerformingPlayer() {
        return _action.getPerformingPlayer();
    }

    @Override
    public void setActionTimeword(Phase phase) {
        _action.setActionTimeword(phase);
    }

    @Override
    public void setPerformingPlayer(String playerId) {
        _action.setPerformingPlayer(playerId);
    }

    @Override
    public String getText(LotroGame game) {
        return _action.getText(game);
    }

    @Override
    public Effect nextEffect(LotroGame game) {
        if (!isCostFailed()) {
            Effect cost = getNextCost();
            if (cost != null)
                return cost;

            Effect effect = getNextEffect();
            if (effect != null)
                return effect;
        }
        return null;
    }
}
