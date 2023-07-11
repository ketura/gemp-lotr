package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.UnrespondableEffect;

public class ShuffleDeckEffect extends UnrespondableEffect {
    private final String _playerId;

    public ShuffleDeckEffect(String playerId) {
        _playerId = playerId;
    }

    @Override
    public void doPlayEffect(DefaultGame game) {
        game.getGameState().sendMessage(_playerId + " shuffles their deck");
        game.getGameState().shuffleDeck(_playerId);
    }
}
