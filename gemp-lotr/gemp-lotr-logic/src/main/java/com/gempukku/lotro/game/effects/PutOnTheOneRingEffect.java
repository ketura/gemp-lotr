package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.AbstractEffect;
import com.gempukku.lotro.game.timing.Effect;
import com.gempukku.lotro.game.timing.results.PutOnTheOneRingResult;

public class PutOnTheOneRingEffect extends AbstractEffect {

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Put on The One Ring";
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return !game.getGameState().isWearingRing();
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        boolean canPutOnTheRing = !game.getGameState().isWearingRing();

        if (canPutOnTheRing) {
            game.getGameState().sendMessage("Ring-bearer puts on The One Ring");
            game.getGameState().setWearingRing(true);
            game.getActionsEnvironment().emitEffectResult(new PutOnTheOneRingResult());
            return new FullEffectResult(true);
        } else {
            return new FullEffectResult(false);
        }
    }
}
