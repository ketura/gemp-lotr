package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.RemoveBurdenResult;

public class RemoveBurdenEffect extends AbstractEffect {
    @Override
    public boolean canPlayEffect(LotroGame game) {
        return game.getGameState().getBurdens() > 0;
    }

    @Override
    public EffectResult getRespondableResult() {
        return new RemoveBurdenResult();
    }

    @Override
    public String getText() {
        return "Remove a burden";
    }

    @Override
    public void playEffect(LotroGame game) {
        game.getGameState().removeBurdens(1);
    }
}
