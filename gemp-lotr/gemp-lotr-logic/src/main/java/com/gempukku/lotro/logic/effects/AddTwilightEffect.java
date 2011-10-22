package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.Preventable;
import com.gempukku.lotro.logic.timing.results.AddTwilightResult;

import java.util.Collections;

public class AddTwilightEffect extends AbstractEffect implements Preventable {
    private PhysicalCard _source;
    private int _twilight;
    private int _prevented;

    public AddTwilightEffect(PhysicalCard source, int twilight) {
        _source = source;
        _twilight = twilight;
    }

    public PhysicalCard getSource() {
        return _source;
    }

    @Override
    public String getText(LotroGame game) {
        return "Add (4)";
    }

    @Override
    public Effect.Type getType() {
        return Effect.Type.BEFORE_ADD_TWILIGHT;
    }

    @Override
    public boolean isPrevented() {
        return _prevented == _twilight;
    }

    @Override
    public void prevent() {
        _prevented = _twilight;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (!isPrevented()) {
            game.getGameState().sendMessage(_twilight + " gets added to the twilight pool");
            game.getGameState().addTwilight(_twilight);
            return new FullEffectResult(Collections.singleton(new AddTwilightResult(_source)), true, _prevented == 0);
        }
        return new FullEffectResult(null, true, false);
    }
}
