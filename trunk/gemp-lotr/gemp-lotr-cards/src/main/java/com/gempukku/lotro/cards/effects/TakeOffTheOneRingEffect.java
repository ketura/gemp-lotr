package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.ChooseableEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

public class TakeOffTheOneRingEffect implements ChooseableEffect {
    @Override
    public String getText(LotroGame game) {
        return "Take off The One Ring";
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return game.getGameState().isWearingRing();
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
