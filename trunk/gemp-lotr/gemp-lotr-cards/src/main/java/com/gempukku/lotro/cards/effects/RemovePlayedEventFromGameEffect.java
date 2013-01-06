package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;

public class RemovePlayedEventFromGameEffect extends AbstractEffect {
    private PlayEventAction _action;

    public RemovePlayedEventFromGameEffect(PlayEventAction action) {
        _action = action;
    }

    @Override
    public String getText(LotroGame game) {
        return "Remove " + GameUtils.getCardLink(_action.getEventPlayed()) + " from the game";
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _action.getEventPlayed().getZone() == Zone.VOID;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            game.getGameState().sendMessage(_action.getPerformingPlayer() + " removes " + GameUtils.getCardLink(_action.getEventPlayed()) + " from the game");
            _action.skipDiscardPart();
            game.getGameState().addCardToZone(game, _action.getEventPlayed(), Zone.REMOVED);
            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}