package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;

import java.util.Collections;

public class StackPlayedEventOnACardEffect extends AbstractEffect {
    private PlayEventAction _action;
    private PhysicalCard _stackOn;

    public StackPlayedEventOnACardEffect(PlayEventAction action, PhysicalCard stackOn) {
        _action = action;
        _stackOn = stackOn;
    }

    @Override
    public String getText(LotroGame game) {
        return "Stack " + GameUtils.getCardLink(_action.getEventPlayed()) + " on "+GameUtils.getCardLink(_stackOn);
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _stackOn.getZone().isInPlay() && _action.getEventPlayed().getZone() == Zone.VOID;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            PhysicalCard eventPlayed = _action.getEventPlayed();
            game.getGameState().sendMessage(_action.getPerformingPlayer() + " stacks " + GameUtils.getCardLink(eventPlayed) + " on " + GameUtils.getCardLink(_stackOn));
            _action.skipDiscardPart();
            game.getGameState().removeCardsFromZone(eventPlayed.getOwner(), Collections.singletonList(eventPlayed));
            game.getGameState().stackCard(game, eventPlayed, _stackOn);
            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}