package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DrawCardOrPutIntoHandResult;

public class DrawCardEffect extends AbstractEffect {
    private String _playerId;
    private final int _count;

    public DrawCardEffect(String playerId, int count) {
        _playerId = playerId;
        _count = count;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.DRAW_CARD_OR_PUT_INTO_HAND;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getGameState().getDeck(_playerId).size() >= _count;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        int drawn = 0;
        for (int i = 0; i < _count; i++) {
            if (game.getGameState().getDeck(_playerId).size() > 0 && game.getModifiersQuerying().canDrawCardAndIncrement(game.getGameState(), _playerId)) {
                game.getGameState().playerDrawsCard(_playerId);
                drawn++;
            }
        }
        game.getGameState().sendMessage(_playerId + " draws " + drawn + " card(s)");

        if (drawn > 0)
            return new FullEffectResult(new EffectResult[]{new DrawCardOrPutIntoHandResult(_playerId, drawn)}, _count == drawn, _count == drawn);
        else
            return new FullEffectResult(null, false, false);
    }
}
