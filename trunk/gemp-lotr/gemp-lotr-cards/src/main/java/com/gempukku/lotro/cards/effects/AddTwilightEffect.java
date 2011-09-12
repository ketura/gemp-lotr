package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class AddTwilightEffect extends UnrespondableEffect {
    private int _twilight;

    public AddTwilightEffect(int twilight) {
        _twilight = twilight;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        game.getGameState().addTwilight(_twilight);
    }
}
