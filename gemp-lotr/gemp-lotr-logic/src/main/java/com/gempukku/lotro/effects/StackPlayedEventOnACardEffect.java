package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.actions.lotronly.PlayEventAction;

import java.util.Collections;

public class StackPlayedEventOnACardEffect extends AbstractEffect {
    private final PlayEventAction _action;
    private final LotroPhysicalCard _stackOn;

    public StackPlayedEventOnACardEffect(PlayEventAction action, LotroPhysicalCard stackOn) {
        _action = action;
        _stackOn = stackOn;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Stack " + GameUtils.getFullName(_action.getEventPlayed()) + " on "+GameUtils.getFullName(_stackOn);
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        Zone zone = _action.getEventPlayed().getZone();
        return _stackOn.getZone().isInPlay() && (zone == Zone.VOID || zone == Zone.VOID_FROM_HAND);
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (isPlayableInFull(game)) {
            LotroPhysicalCard eventPlayed = _action.getEventPlayed();
            game.getGameState().sendMessage(_action.getPerformingPlayer() + " stacks " + GameUtils.getCardLink(eventPlayed) + " on " + GameUtils.getCardLink(_stackOn));
            game.getGameState().removeCardsFromZone(eventPlayed.getOwner(), Collections.singletonList(eventPlayed));
            game.getGameState().stackCard(game, eventPlayed, _stackOn);
            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}