package com.gempukku.lotro.logic.timing.actions;

import com.gempukku.lotro.game.PhysicalCard;
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
    public PhysicalCard getActionSource() {
        return null;
    }

    @Override
    public String getText() {
        return _text;
    }

    @Override
    public Effect nextEffect() {
        Effect result = _effect;
        _effect = null;
        return result;
    }
}
