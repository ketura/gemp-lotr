package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class TakeOffTheOneRingEffect extends UnrespondableEffect {
    @Override
    public boolean canPlayEffect(LotroGame game) {
        return game.getGameState().isWearingRing();
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        game.getGameState().sendMessage("Ring-bearer takes off The One Ring");
        game.getGameState().setWearingRing(false);
    }
}
