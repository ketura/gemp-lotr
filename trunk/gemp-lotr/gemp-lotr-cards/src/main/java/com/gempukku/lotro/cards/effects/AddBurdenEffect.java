package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.ChooseableCost;
import com.gempukku.lotro.logic.timing.ChooseableEffect;
import com.gempukku.lotro.logic.timing.CostResolution;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class AddBurdenEffect extends UnrespondableEffect implements ChooseableEffect, ChooseableCost {
    private String _playerId;
    private int _count;

    public AddBurdenEffect(String playerId) {
        this(playerId, 1);
    }

    public AddBurdenEffect(String playerId, int count) {
        _playerId = playerId;
        _count = count;
    }

    @Override
    public String getText(LotroGame game) {
        return "Add " + _count + " burden(s)";
    }

    @Override
    public boolean canPlayCost(LotroGame game) {
        return true;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return true;
    }

    @Override
    public CostResolution playCost(LotroGame game) {
        game.getGameState().sendMessage(_playerId + " adds " + _count + " burden(s)");
        game.getGameState().addBurdens(_count);
        return new CostResolution(null, true);
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        game.getGameState().sendMessage(_playerId + " adds " + _count + " burden(s)");
        game.getGameState().addBurdens(_count);
    }
}
