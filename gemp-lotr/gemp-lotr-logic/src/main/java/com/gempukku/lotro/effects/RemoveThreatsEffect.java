package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.GameUtils;

public class RemoveThreatsEffect extends AbstractEffect {
    private final LotroPhysicalCard _source;
    private final int _count;

    public RemoveThreatsEffect(LotroPhysicalCard source, int count) {
        _source = source;
        _count = count;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Remove " + _count + " threat" + ((_count > 1) ? "s" : "");
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return game.getGameState().getThreats() >= _count && game.getModifiersQuerying().canRemoveThreat(game, _source);
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (_source == null || game.getModifiersQuerying().canRemoveThreat(game, _source)) {
            int toRemove = Math.min(game.getGameState().getThreats(), _count);

            if (toRemove > 0) {
                if (_source != null)
                    game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " removed " + GameUtils.formatNumber(toRemove, _count) + " threat" + ((toRemove > 1) ? "s" : ""));
                game.getGameState().removeThreats(game.getGameState().getCurrentPlayerId(), toRemove);
            }

            return new FullEffectResult(_count == toRemove);
        }
        return new FullEffectResult(false);
    }
}
