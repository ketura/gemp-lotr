package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Cost;
import com.gempukku.lotro.logic.timing.CostResolution;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

public class AddTwilightEffect implements Effect, Cost {
    private int _twilight;

    public AddTwilightEffect(int twilight) {
        _twilight = twilight;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        game.getGameState().sendMessage(_twilight + " gets added to the twilight pool");
        game.getGameState().addTwilight(_twilight);
        return null;
    }

    @Override
    public CostResolution playCost(LotroGame game) {
        game.getGameState().sendMessage(_twilight + " gets added to the twilight pool");
        game.getGameState().addTwilight(_twilight);
        return new CostResolution(null, true);
    }
}
