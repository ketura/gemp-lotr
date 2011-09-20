package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PutOnTheOneRingResult;

public class PutOnTheOneRingEffect extends AbstractEffect {

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.PUT_ON_THE_ONE_RING;
    }

    @Override
    public String getText(LotroGame game) {
        return "Ring-bearer puts on The One Ring";
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return !game.getGameState().isWearingRing();
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        game.getGameState().sendMessage("Ring-bearer puts on The One Ring");
        game.getGameState().setWearingRing(true);
        return new EffectResult[]{new PutOnTheOneRingResult()};
    }
}
