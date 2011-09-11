package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class CorruptRingBearerEffect extends UnrespondableEffect {
    @Override
    public void playEffect(LotroGame game) {
        game.getGameState().setLoserPlayerId(game.getGameState().getCurrentPlayerId());
    }
}
