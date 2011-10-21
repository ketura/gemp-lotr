package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.Preventable;
import com.gempukku.lotro.logic.timing.results.AddBurdenResult;

import java.util.Collections;

public class AddBurdenEffect extends AbstractEffect implements Preventable {
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

    @Override
    public boolean isPrevented() {
        return _prevented == _count;
    }

    @Override
    public void prevent() {
        _prevented++;
    }

    public void preventAll() {
        _prevented = _count;
    }

    @Override
    public String getText(LotroGame game) {
        return "Add " + (_count - _prevented) + "burden" + (((_count - _prevented) > 1) ? "s" : "");
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }

    @Override
    public Effect.Type getType() {
        return Type.BEFORE_ADD_BURDENS;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (_prevented < _count) {
            int toAdd = _count - _prevented;
            game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " adds " + toAdd + " burden" + ((toAdd > 1) ? "s" : ""));
            game.getGameState().addBurdens(toAdd);
            return new FullEffectResult(Collections.singleton(new AddBurdenResult(_source, toAdd)), true, _prevented == 0);
        }
        return new FullEffectResult(null, true, false);
    }
}
