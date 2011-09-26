package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.ChooseableEffect;
import com.gempukku.lotro.logic.timing.Cost;
import com.gempukku.lotro.logic.timing.CostResolution;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class AddBurdenEffect extends UnrespondableEffect implements ChooseableEffect, Cost {
    private String _playerId;

    public AddBurdenEffect(String playerId) {
        _playerId = playerId;
    }

    @Override
    public String getText(LotroGame game) {
        return "Add a burden";
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return true;
    }

    @Override
    public CostResolution playCost(LotroGame game) {
        game.getGameState().sendMessage(_playerId + " adds a burden");
        game.getGameState().addBurdens(1);
        return new CostResolution(null, true);
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        game.getGameState().sendMessage(_playerId + " adds a burden");
        game.getGameState().addBurdens(1);
    }
}
