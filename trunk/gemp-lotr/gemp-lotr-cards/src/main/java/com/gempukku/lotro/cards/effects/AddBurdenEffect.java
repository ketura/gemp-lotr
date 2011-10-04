package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.AddBurdenResult;

public class AddBurdenEffect extends AbstractEffect {
    private PhysicalCard _source;
    private int _count;
    private int _prevented;

    public AddBurdenEffect(PhysicalCard source, int count) {
        _source = source;
        _count = count;
    }

    public PhysicalCard getSource() {
        return _source;
    }

    public boolean isFullyPrevented() {
        return _prevented == _count;
    }

    public void prevent() {
        _prevented++;
    }

    @Override
    public String getText(LotroGame game) {
        return "Add " + (_count - _prevented) + "burden(s)";
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.ADD_BURDEN;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (_prevented < _count) {
            int toAdd = _count - _prevented;
            game.getGameState().sendMessage(_source.getBlueprint().getName() + " adds " + toAdd + " burden(s)");
            game.getGameState().addBurdens(toAdd);
            return new FullEffectResult(new EffectResult[]{new AddBurdenResult(_source, toAdd)}, true, _prevented == 0);
        }
        return new FullEffectResult(null, true, false);
    }
}
