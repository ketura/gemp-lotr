package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractSuccessfulEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;

public class TriggeringResultEffect extends AbstractSuccessfulEffect {
    private Type _effectType;
    private EffectResult _effectResult;
    private String _text;

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
    public Collection<? extends EffectResult> playEffect(LotroGame game) {
        return Collections.singleton(_effectResult);
    }
}
