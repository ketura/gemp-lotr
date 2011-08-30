package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class TakeOffTheOneRingEffect extends UnrespondableEffect {
    @Override
    public boolean canPlayEffect(LotroGame game) {
        return game.getGameState().isWearingRing();
    }

    @Override
    public void playEffect(LotroGame game) {
        game.getGameState().setWearingRing(false);
    }
}
