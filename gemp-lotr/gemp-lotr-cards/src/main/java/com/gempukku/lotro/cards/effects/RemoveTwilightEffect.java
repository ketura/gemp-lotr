package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class RemoveTwilightEffect extends UnrespondableEffect {
    private int _twilight;

    public RemoveTwilightEffect(int twilight) {
        _twilight = twilight;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return _twilight <= game.getGameState().getTwilightPool();
    }

    @Override
    public void playEffect(LotroGame game) {
        game.getGameState().removeTwilight(_twilight);
    }
}
