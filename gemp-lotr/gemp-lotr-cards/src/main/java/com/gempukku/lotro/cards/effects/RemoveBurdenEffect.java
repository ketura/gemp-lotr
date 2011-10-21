package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.RemoveBurdenResult;

import java.util.Collections;

public class RemoveBurdenEffect extends AbstractEffect {
    private PhysicalCard _source;

    public RemoveBurdenEffect(PhysicalCard source) {
        _source = source;
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
        return "Remove a burden";
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getModifiersQuerying().canRemoveBurden(game.getGameState(), _source)
                && game.getGameState().getBurdens() > 0;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            game.getGameState().sendMessage("Removed a burden");
            game.getGameState().removeBurdens(1);
            return new FullEffectResult(Collections.singleton(new RemoveBurdenResult(_source)), true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}
