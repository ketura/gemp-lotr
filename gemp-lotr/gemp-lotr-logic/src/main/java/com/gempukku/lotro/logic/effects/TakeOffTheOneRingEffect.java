package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.PutOnTheOneRingResult;
import com.gempukku.lotro.logic.timing.results.TakeOffTheOneRingResult;

public class TakeOffTheOneRingEffect extends AbstractEffect {
    @Override
    public String getText(LotroGame game) {
        return "Take off The One Ring";
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getGameState().isWearingRing();
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        boolean canTakeOffRing = game.getGameState().isWearingRing();
        if (canTakeOffRing) {
            game.getGameState().sendMessage("Ring-bearer takes off The One Ring");
            game.getGameState().setWearingRing(false);
            game.getActionsEnvironment().emitEffectResult(new TakeOffTheOneRingResult());
        }
        return new FullEffectResult(canTakeOffRing);
    }
}
