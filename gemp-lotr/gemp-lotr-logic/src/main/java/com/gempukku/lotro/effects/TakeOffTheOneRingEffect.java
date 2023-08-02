package com.gempukku.lotro.effects;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.effects.results.TakeOffTheOneRingResult;

public class TakeOffTheOneRingEffect extends AbstractEffect {
    @Override
    public String getText(DefaultGame game) {
        return "Take off The One Ring";
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return game.getGameState().isWearingRing();
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        boolean canTakeOffRing = game.getGameState().isWearingRing();
        if (canTakeOffRing) {
            game.getGameState().sendMessage("Ring-bearer takes off The One Ring");
            game.getGameState().setWearingRing(false);
            game.getActionsEnvironment().emitEffectResult(new TakeOffTheOneRingResult());
        }
        return new FullEffectResult(canTakeOffRing);
    }
}
