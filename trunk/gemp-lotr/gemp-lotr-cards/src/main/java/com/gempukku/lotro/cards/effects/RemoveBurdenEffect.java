package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.RemoveBurdenResult;

public class RemoveBurdenEffect extends AbstractEffect {
    private String _playerId;

    public RemoveBurdenEffect(String playerId) {
        _playerId = playerId;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return game.getGameState().getBurdens(_playerId) > 0;
    }

    @Override
    public EffectResult getRespondableResult() {
        return new RemoveBurdenResult(_playerId);
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
