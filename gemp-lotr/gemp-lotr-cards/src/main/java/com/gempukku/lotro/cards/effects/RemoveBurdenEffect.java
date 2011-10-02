package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.ChooseableEffect;
import com.gempukku.lotro.logic.timing.Cost;
import com.gempukku.lotro.logic.timing.CostResolution;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.RemoveBurdenResult;

public class RemoveBurdenEffect implements ChooseableEffect, Cost {
    private PhysicalCard _source;

    public RemoveBurdenEffect(PhysicalCard source) {
        _source = source;
    }

    public PhysicalCard getSource() {
        return _source;
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.REMOVE_BURDEN;
    }

    @Override
    public String getText(LotroGame game) {
        return "Remove a burden";
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return game.getGameState().getBurdens() > 0;
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        if (game.getGameState().getBurdens() > 0) {
            game.getGameState().sendMessage("Removed a burden");
            game.getGameState().removeBurdens(1);
            return new EffectResult[]{new RemoveBurdenResult(_source)};
        }
        return null;
    }

    @Override
    public CostResolution playCost(LotroGame game) {
        if (game.getGameState().getBurdens() > 0) {
            game.getGameState().sendMessage("Removed a burden");
            game.getGameState().removeBurdens(1);
            return new CostResolution(new EffectResult[]{new RemoveBurdenResult(_source)}, true);
        }
        return new CostResolution(null, false);
    }
}
