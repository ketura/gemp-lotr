package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.RemoveBurdenResult;

public class RemoveBurdenEffect extends AbstractEffect {
    private String _performingPlayerId;
    private PhysicalCard _source;
    private int _count;

    public RemoveBurdenEffect(String performingPlayerId, PhysicalCard source) {
        this(performingPlayerId, source, 1);
    }

    public RemoveBurdenEffect(String performingPlayerId, PhysicalCard source, int count) {
        _performingPlayerId = performingPlayerId;
        _source = source;
        _count = count;
    }

    public PhysicalCard getSource() {
        return _source;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return "Remove " + _count + " burden" + ((_count > 1) ? "s" : "");
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getModifiersQuerying().canRemoveBurden(game.getGameState(), _source)
                && game.getGameState().getBurdens() >= _count;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (game.getModifiersQuerying().canRemoveBurden(game.getGameState(), _source)) {
            int toRemove = Math.min(_count, game.getGameState().getBurdens());
            if (toRemove > 0) {
                game.getGameState().sendMessage(_performingPlayerId + " removed " + toRemove + " burden" + ((toRemove > 1) ? "s" : "") + " with " + GameUtils.getCardLink(_source));
                game.getGameState().removeBurdens(toRemove);
                for (int i = 0; i < toRemove; i++)
                    game.getActionsEnvironment().emitEffectResult(new RemoveBurdenResult(_performingPlayerId, _source));
                return new FullEffectResult(true, true);
            }
        }

        return new FullEffectResult(false, false);
    }
}
