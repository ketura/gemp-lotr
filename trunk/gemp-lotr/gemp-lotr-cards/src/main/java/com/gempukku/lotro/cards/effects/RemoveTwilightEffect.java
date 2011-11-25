package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;

public class RemoveTwilightEffect extends AbstractEffect {
    private int _twilight;

    public RemoveTwilightEffect(int twilight) {
        _twilight = twilight;
    }

    @Override
    public String getText(LotroGame game) {
        return "Remove (" + _twilight + ")";
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getGameState().getTwilightPool() >= _twilight;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        int toRemove = Math.min(game.getGameState().getTwilightPool(), _twilight);
        game.getGameState().sendMessage(toRemove + " twilight gets removed from twilight pool");
        game.getGameState().removeTwilight(toRemove);

        return new FullEffectResult(toRemove == _twilight, toRemove == _twilight);
    }
}
