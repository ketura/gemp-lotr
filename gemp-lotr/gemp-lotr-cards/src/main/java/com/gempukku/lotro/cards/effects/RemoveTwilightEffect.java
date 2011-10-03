package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.ChooseableCost;
import com.gempukku.lotro.logic.timing.CostResolution;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class RemoveTwilightEffect extends UnrespondableEffect implements ChooseableCost {
    private int _twilight;

    public RemoveTwilightEffect(int twilight) {
        _twilight = twilight;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        int toRemove = Math.min(game.getGameState().getTwilightPool(), _twilight);
        game.getGameState().sendMessage(toRemove + " twilight gets removed from twilight pool");
        game.getGameState().removeTwilight(toRemove);
    }

    @Override
    public boolean canPlayCost(LotroGame game) {
        return game.getGameState().getTwilightPool() >= _twilight;
    }

    @Override
    public CostResolution playCost(LotroGame game) {
        int toRemove = Math.min(game.getGameState().getTwilightPool(), _twilight);
        game.getGameState().sendMessage(toRemove + " twilight gets removed from twilight pool");
        game.getGameState().removeTwilight(toRemove);

        return new CostResolution(null, toRemove == _twilight);
    }
}
