package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

public class TakeOffTheOneRingEffect extends AbstractEffect {
    @Override
    public String getText(LotroGame game) {
        return "Take off The One Ring";
    }

    @Override
    public EffectResult.Type getType() {
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
        }
        return new FullEffectResult(null, canTakeOffRing, canTakeOffRing);
    }
}
