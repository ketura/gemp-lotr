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
        return "Remove " + _count + " threat" + ((_count > 1) ? "s" : "");
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getGameState().getThreats() >= _count && game.getModifiersQuerying().canRemoveThreat(game.getGameState(), _source);
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (game.getModifiersQuerying().canRemoveThreat(game.getGameState(), _source)) {
            int toRemove = Math.min(game.getGameState().getThreats(), _count);

            if (_source != null)
                game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " removed " + toRemove + " threat" + ((toRemove > 1) ? "s" : ""));

            game.getGameState().removeThreats(game.getGameState().getCurrentPlayerId(), toRemove);

            return new FullEffectResult(null, _count == toRemove, _count == toRemove);
        }
        return new FullEffectResult(null, false, false);
    }
}
