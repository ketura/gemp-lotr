package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.results.KilledResult;
import com.gempukku.lotro.game.timing.results.ThreatWoundTriggerResult;

public class ThreatWoundsEffect extends AbstractEffect {
    private final KilledResult _killResult;

    public ThreatWoundsEffect(KilledResult killResult) {
        _killResult = killResult;
    }

    public KilledResult getKillResult() {
        return _killResult;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Before threat wounds are placed";
    }

    @Override
    public Type getType() {
        return Type.BEFORE_THREAT_WOUNDS;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        game.getActionsEnvironment().emitEffectResult(new ThreatWoundTriggerResult());
        return new FullEffectResult(true);
    }
}
