package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.game.timing.results.PlayCardResult;

import java.util.Collections;

public class PlayCardEffect extends AbstractEffect {
    private final Zone _playedFrom;
    private final LotroPhysicalCard _cardPlayed;
    private LotroPhysicalCard _attachedToCard;
    private final Zone _zone;
    private final LotroPhysicalCard _attachedOrStackedPlayedFrom;

    public PlayCardEffect(Zone playedFrom, LotroPhysicalCard cardPlayed, Zone playedTo, LotroPhysicalCard attachedOrStackedPlayedFrom) {
        _playedFrom = playedFrom;
        _cardPlayed = cardPlayed;
        _zone = playedTo;
        _attachedOrStackedPlayedFrom = attachedOrStackedPlayedFrom;
    }

    public PlayCardEffect(Zone playedFrom, LotroPhysicalCard cardPlayed, LotroPhysicalCard attachedToCard, LotroPhysicalCard attachedOrStackedPlayedFrom) {
        _playedFrom = playedFrom;
        _cardPlayed = cardPlayed;
        _attachedToCard = attachedToCard;
        _attachedOrStackedPlayedFrom = attachedOrStackedPlayedFrom;
        _zone = Zone.ATTACHED;
    }

    public LotroPhysicalCard getPlayedCard() {
        return _cardPlayed;
    }

    public LotroPhysicalCard getAttachedTo() {
        return _attachedToCard;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Play " + GameUtils.getFullName(_cardPlayed);
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        game.getGameState().removeCardsFromZone(_cardPlayed.getOwner(), Collections.singleton(_cardPlayed));
        if (_attachedToCard != null) {
            game.getGameState().attachCard(game, _cardPlayed, _attachedToCard);
        } else {
            game.getGameState().addCardToZone(game, _cardPlayed, _zone);
        }

        game.getActionsEnvironment().emitEffectResult(new PlayCardResult(_playedFrom, _cardPlayed, _attachedToCard, _attachedOrStackedPlayedFrom));

        return new FullEffectResult(true);
    }
}
