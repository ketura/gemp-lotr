package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class CancelSkirmishEffect extends UnrespondableEffect {
    @Override
    public void playEffect(LotroGame game) {
        game.getGameState().getSkirmish().cancel();
    }
}
