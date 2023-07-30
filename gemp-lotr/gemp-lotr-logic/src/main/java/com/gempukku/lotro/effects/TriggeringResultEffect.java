package com.gempukku.lotro.effects;

import com.gempukku.lotro.game.DefaultGame;

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
    public String getText(DefaultGame game) {
        return _text;
    }

    @Override
    public void playEffect(DefaultGame game) {
        game.getActionsEnvironment().emitEffectResult(_effectResult);
    }
}
