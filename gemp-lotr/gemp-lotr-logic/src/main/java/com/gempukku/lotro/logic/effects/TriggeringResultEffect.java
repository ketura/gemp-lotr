package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractSuccessfulEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

public class TriggeringResultEffect extends AbstractSuccessfulEffect {
    private final Type _effectType;
    private final EffectResult _effectResult;
    private final String _text;

    public TriggeringResultEffect(EffectResult effectResult, String text) {
        this(null, effectResult, text);
    }

    public TriggeringResultEffect(Effect.Type effectType, EffectResult effectResult, String text) {
        _effectType = effectType;
        _effectResult = effectResult;
        _text = text;
    }

    @Override
    public Effect.Type getType() {
        return _effectType;
    }

    @Override
    public String getText(LotroGame game) {
        return _text;
    }

    @Override
    public void playEffect(LotroGame game) {
        game.getActionsEnvironment().emitEffectResult(_effectResult);
    }
}
