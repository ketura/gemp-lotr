package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class DrawCardEffect extends UnrespondableEffect {
    private String _playerId;

    public DrawCardEffect(String playerId) {
        _playerId = playerId;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return game.getGameState().getDeck(_playerId).size() > 0;
    }

    @Override
    public void playEffect(LotroGame game) {
        game.getGameState().playerDrawsCard(_playerId);
    }
}
