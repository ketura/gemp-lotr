package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.state.LotroGame;

public abstract class AbstractSuccessfulEffect implements Effect {
    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }

    @Override
    public boolean wasSuccessful() {
        return true;
    }

    @Override
    public boolean wasCarriedOut() {
        return true;
    }

    @Override
    public void reset() {

    }
}
