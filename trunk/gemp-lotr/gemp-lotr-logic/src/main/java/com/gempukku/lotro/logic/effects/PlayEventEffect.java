package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.results.PlayEventResult;

import java.util.Collections;

public class PlayEventEffect extends PlayCardEffect {
    private PhysicalCard _cardPlayed;
    private PlayEventResult _playEventResult;

    public PlayEventEffect(Zone playedFrom, PhysicalCard cardPlayed, boolean requiresRanger) {
        super(playedFrom, cardPlayed, (Zone) null, null);
        _cardPlayed = cardPlayed;
        _playEventResult = new PlayEventResult(playedFrom, getPlayedCard(), requiresRanger);
    }

    public PlayEventResult getPlayEventResult() {
        return _playEventResult;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (_cardPlayed.getZone() != null)
            game.getGameState().removeCardsFromZone(_cardPlayed.getOwner(), Collections.singleton(_cardPlayed));
        game.getActionsEnvironment().emitEffectResult(_playEventResult);
        return new FullEffectResult(true, true);
    }
}
