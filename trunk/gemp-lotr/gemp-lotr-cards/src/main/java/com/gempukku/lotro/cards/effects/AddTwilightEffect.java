package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Cost;
import com.gempukku.lotro.logic.timing.CostResolution;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.AddTwilightResult;

public class AddTwilightEffect implements Effect, Cost {
    private PhysicalCard _source;
    private boolean _prevented;
    private int _twilight;

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

    public void prevent() {
        _prevented = true;
    }

    public boolean isPrevented() {
        return _prevented;
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        if (!isPrevented()) {
            game.getGameState().sendMessage(_twilight + " gets added to the twilight pool");
            game.getGameState().addTwilight(_twilight);
            return new EffectResult[]{new AddTwilightResult(_source)};
        }
        return null;
    }

    @Override
    public CostResolution playCost(LotroGame game) {
        if (!isPrevented()) {
            game.getGameState().sendMessage(_twilight + " gets added to the twilight pool");
            game.getGameState().addTwilight(_twilight);
            return new CostResolution(new EffectResult[]{new AddTwilightResult(_source)}, true);
        }
        return new CostResolution(null, true);
    }
}
