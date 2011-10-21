package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.PutOnTheOneRingResult;

import java.util.Collections;

public class PutOnTheOneRingEffect extends AbstractEffect {

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return "Ring-bearer puts on The One Ring";
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return !game.getGameState().isWearingRing();
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        boolean canPutOnTheRing = !game.getGameState().isWearingRing();

        if (canPutOnTheRing) {
            game.getGameState().sendMessage("Ring-bearer puts on The One Ring");
            game.getGameState().setWearingRing(true);
            return new FullEffectResult(Collections.singleton(new PutOnTheOneRingResult()), true, true);
        } else {
            return new FullEffectResult(null, false, false);
        }
    }
}
