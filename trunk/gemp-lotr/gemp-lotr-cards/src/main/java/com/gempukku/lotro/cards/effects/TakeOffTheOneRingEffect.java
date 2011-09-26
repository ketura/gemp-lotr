package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

public class TakeOffTheOneRingEffect implements Effect {
    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        boolean canTakeOffRing = game.getGameState().isWearingRing();
        if (canTakeOffRing) {
            game.getGameState().sendMessage("Ring-bearer takes off The One Ring");
            game.getGameState().setWearingRing(false);
        }
        return null;
    }
}
