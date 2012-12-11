package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.results.KilledResult;
import com.gempukku.lotro.logic.timing.results.ThreatWoundTriggerResult;

public class ThreatWoundsEffect extends AbstractEffect {
    private KilledResult _killResult;

    public ThreatWoundsEffect(KilledResult killResult) {
        _killResult = killResult;
    }

    public KilledResult getKillResult() {
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
        game.getActionsEnvironment().emitEffectResult(new ThreatWoundTriggerResult());
        return new FullEffectResult(true);
    }
}
