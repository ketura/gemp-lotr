package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractSuccessfulEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

public class TriggeringEffect extends AbstractSuccessfulEffect {
    private EffectResult _effectResult;
    private String _text;

    public TriggeringEffect(EffectResult effectResult, String text) {
        _effectResult = effectResult;
        _text = text;
    }

    @Override
    public EffectResult.Type getType() {
        return _effectResult.getType();
    }

    @Override
    public String getText(LotroGame game) {
        return _text;
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        return new EffectResult[]{_effectResult};
    }
}
