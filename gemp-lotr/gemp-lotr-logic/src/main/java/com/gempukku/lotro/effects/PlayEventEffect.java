package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.lotronly.PlayEventAction;
import com.gempukku.lotro.effects.results.PlayEventResult;

import java.util.Collections;

public class PlayEventEffect extends PlayCardEffect {
    private final LotroPhysicalCard _cardPlayed;
    private final PlayEventResult _playEventResult;

    public PlayEventEffect(PlayEventAction action, Zone playedFrom, LotroPhysicalCard cardPlayed, boolean requiresRanger, boolean paidToil) {
        super(playedFrom, cardPlayed, (Zone) null, null);
        _cardPlayed = cardPlayed;
        _playEventResult = new PlayEventResult(action, playedFrom, getPlayedCard(), requiresRanger);
    }

    public PlayEventResult getPlayEventResult() {
        return _playEventResult;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (_cardPlayed.getZone() == Zone.VOID_FROM_HAND) {
            // At this point, card should change initiative if played
            game.getGameState().removeCardsFromZone(_cardPlayed.getOwner(), Collections.singleton(_cardPlayed));
            game.getGameState().addCardToZone(game, _cardPlayed, Zone.VOID);
        }

        game.getActionsEnvironment().emitEffectResult(_playEventResult);
        return new FullEffectResult(true);
    }
}
