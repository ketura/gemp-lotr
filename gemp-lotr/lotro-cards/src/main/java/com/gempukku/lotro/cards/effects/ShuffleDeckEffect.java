package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class ShuffleDeckEffect extends UnrespondableEffect {
    private String _playerId;

    public ShuffleDeckEffect(String playerId) {
        _playerId = playerId;
    }

    @Override
    public void playEffect(LotroGame game) {
        game.getGameState().shuffleDeck(_playerId);
    }
}
