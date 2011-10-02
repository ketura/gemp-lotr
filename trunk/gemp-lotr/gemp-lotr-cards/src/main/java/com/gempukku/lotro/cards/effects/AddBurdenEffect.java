package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.ChooseableCost;
import com.gempukku.lotro.logic.timing.ChooseableEffect;
import com.gempukku.lotro.logic.timing.CostResolution;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.AddBurdenResult;

public class AddBurdenEffect implements ChooseableEffect, ChooseableCost {
    private PhysicalCard _source;
    private boolean _prevented;

    public AddBurdenEffect(PhysicalCard source) {
        _source = source;
    }

    public PhysicalCard getSource() {
        return _source;
    }

    public boolean isPrevented() {
        return _prevented;
    }

    public void prevent() {
        _prevented = true;
    }

    @Override
    public String getText(LotroGame game) {
        return "Add burden";
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
    public EffectResult.Type getType() {
        return EffectResult.Type.ADD_BURDEN;
    }

    @Override
    public CostResolution playCost(LotroGame game) {
        return new CostResolution(playEffect(game), !_prevented);
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        if (!_prevented) {
            game.getGameState().sendMessage(_source.getBlueprint().getName() + " adds burden");
            game.getGameState().addBurdens(1);
            return new EffectResult[]{new AddBurdenResult(_source)};
        }
        return null;
    }
}
