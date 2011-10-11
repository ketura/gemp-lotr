package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.AddTwilightResult;

public class AddTwilightEffect extends AbstractEffect {
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
        return null;
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.ADD_TWILIGHT;
    }

    public void preventAll() {
        _prevented = _twilight;
    }

    public boolean isFullyPrevented() {
        return _prevented == _twilight;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (!isFullyPrevented()) {
            game.getGameState().sendMessage(_twilight + " gets added to the twilight pool");
            game.getGameState().addTwilight(_twilight);
            return new FullEffectResult(new EffectResult[]{new AddTwilightResult(_source)}, true, _prevented == 0);
        }
        return new FullEffectResult(null, true, false);
    }
}
