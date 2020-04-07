package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.state.LotroGame;

public class DoNothingEffect extends UnrespondableEffect {
    @Override
    protected void doPlayEffect(LotroGame game) {
        // Do nothing
    }
}
