package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;

public class PutPlayedEventOnTopOfDeckEffect extends AbstractEffect {
    private PlayEventAction _action;

    public PutPlayedEventOnTopOfDeckEffect(PlayEventAction action) {
        _action = action;
    }

    @Override
    public String getText(LotroGame game) {
        return "Put " + GameUtils.getCardLink(_action.getActionSource()) + " on top of your deck";
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            game.getGameState().sendMessage(_action.getPerformingPlayer() + " puts " + GameUtils.getCardLink(_action.getActionSource()) + " on top of his/her deck");
            _action.skipDiscardPart();
            game.getGameState().putCardOnTopOfDeck(_action.getActionSource());
            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}
