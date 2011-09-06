package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class RemoveBurdenEffect extends UnrespondableEffect {
    private String _playerId;

    public RemoveBurdenEffect(String playerId) {
        _playerId = playerId;
    }

    @Override
    public String getText() {
        return "Remove a burden";
    }

    @Override
    public void playEffect(LotroGame game) {
        game.getGameState().removeBurdens(_playerId, 1);
    }
}
