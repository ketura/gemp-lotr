package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.Preventable;
import com.gempukku.lotro.logic.timing.results.DrawCardOrPutIntoHandResult;

public class DrawOneCardEffect extends AbstractEffect implements Preventable {
    private String _playerId;
    private boolean _prevented;

    public DrawOneCardEffect(String playerId) {
        _playerId = playerId;
    }

    @Override
    public String getText(LotroGame game) {
        return "Draw a card";
    }

    @Override
    public Effect.Type getType() {
        return Type.BEFORE_DRAW_CARD;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getGameState().getDeck(_playerId).size() >= 1;
    }

    public boolean canDrawCard(LotroGame game) {
        return (!_prevented && game.getGameState().getDeck(_playerId).size() > 0) && game.getModifiersQuerying().canDrawCardNoIncrement(game.getGameState(), _playerId);
    }

    public String getPlayerId() {
        return _playerId;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        int drawn = 0;
        if (!_prevented && game.getGameState().getDeck(_playerId).size() > 0 && game.getModifiersQuerying().canDrawCardAndIncrement(game.getGameState(), _playerId)) {
            game.getGameState().playerDrawsCard(_playerId);
            drawn++;
        }

        if (drawn == 1) {
            game.getActionsEnvironment().emitEffectResult(new DrawCardOrPutIntoHandResult(_playerId, true));
            return new FullEffectResult(true);
        } else
            return new FullEffectResult(false);
    }

    @Override
    public void prevent() {
        _prevented = true;
    }

    @Override
    public boolean isPrevented() {
        return _prevented;
    }
}
