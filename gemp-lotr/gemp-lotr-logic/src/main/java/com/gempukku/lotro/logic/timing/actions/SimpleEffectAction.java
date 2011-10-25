package com.gempukku.lotro.logic.timing.actions;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

public class SimpleEffectAction implements Action {
    private Effect _effect;
    private String _text;

    public SimpleEffectAction(Effect effect, String text) {
        _effect = effect;
        _text = text;
    }

    @Override
    public Phase getActionTimeword() {
        return null;
    }

    @Override
    public void setActionTimeword(Phase phase) {
    }

    @Override
    public String getPerformingPlayer() {
        return null;
    }

    @Override
    public void setPerformingPlayer(String playerId) {
    }

    @Override
    public PhysicalCard getActionSource() {
        return null;
    }

    @Override
    public PhysicalCard getActionAttachedToCard() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return _text;
    }

    @Override
    public Effect nextEffect(LotroGame game) {
        Effect result = _effect;
        _effect = null;
        return result;
    }
}
