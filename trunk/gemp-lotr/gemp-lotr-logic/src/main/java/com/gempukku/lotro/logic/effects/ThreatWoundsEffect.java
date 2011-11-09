package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.results.KillResult;
import com.gempukku.lotro.logic.timing.results.ThreatWoundTriggerResult;

import java.util.Collections;

public class ThreatWoundsEffect extends AbstractEffect {
    private KillResult _killResult;

    public ThreatWoundsEffect(KillResult killResult) {
        _killResult = killResult;
    }

    public KillResult getKillResult() {
        return _killResult;
    }

    @Override
    public String getText(LotroGame game) {
        return "Before threat wounds are placed";
    }

    @Override
    public Type getType() {
        return Type.BEFORE_THREAT_WOUNDS;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        return new FullEffectResult(Collections.singleton(new ThreatWoundTriggerResult()), true, true);
    }
}
