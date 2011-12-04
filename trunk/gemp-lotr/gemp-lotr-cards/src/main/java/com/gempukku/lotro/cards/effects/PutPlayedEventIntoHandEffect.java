package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;

public class PutPlayedEventIntoHandEffect extends AbstractEffect {
    private PlayEventAction _action;

    public PutPlayedEventIntoHandEffect(PlayEventAction action) {
        _action = action;
    }

    @Override
    public String getText(LotroGame game) {
        return "Put " + GameUtils.getCardLink(_action.getEventPlayed()) + " into hand";
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
            game.getGameState().sendMessage(_action.getPerformingPlayer() + " puts " + GameUtils.getCardLink(_action.getEventPlayed()) + " into hand");
            _action.skipDiscardPart();
            game.getGameState().addCardToZone(game, _action.getEventPlayed(), Zone.HAND);
            return new FullEffectResult(true, true);
        }
        return new FullEffectResult(false, false);
    }
}