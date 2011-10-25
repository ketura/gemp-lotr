package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;

public class RemoveThreatsEffect extends AbstractEffect {
    private PhysicalCard _source;
    private int _count;

    public RemoveThreatsEffect(PhysicalCard source, int count) {
        _source = source;
        _count = count;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getGameState().getThreats() >= _count;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        int toRemove = Math.min(game.getGameState().getThreats(), _count);

        if (_source != null)
            game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " removed " + toRemove + " threat" + ((toRemove > 1) ? "s" : ""));

        game.getGameState().removeThreats(game.getGameState().getCurrentPlayerId(), toRemove);

        return new FullEffectResult(null, _count == toRemove, _count == toRemove);
    }
}
