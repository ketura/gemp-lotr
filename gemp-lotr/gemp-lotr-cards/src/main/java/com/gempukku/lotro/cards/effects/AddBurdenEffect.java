package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class AddBurdenEffect extends UnrespondableEffect {
    private String _playerId;

    public AddBurdenEffect(String playerId) {
        _playerId = playerId;
    }

    @Override
    public String getText() {
        return "Add a burden";
    }

    @Override
    public void playEffect(LotroGame game) {
        game.getGameState().addBurdens(_playerId, 1);
    }
}
