package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;

public class PlayCardEffect extends AbstractEffect {
    private PhysicalCard _cardPlayed;
    private PhysicalCard _attachedToCard;
    private Zone _zone;

    public PlayCardEffect(PhysicalCard cardPlayed, Zone zone) {
        _cardPlayed = cardPlayed;
        _zone = zone;
    }

    public PlayCardEffect(PhysicalCard cardPlayed, PhysicalCard attachedToCard) {
        _cardPlayed = cardPlayed;
        _attachedToCard = attachedToCard;
        _zone = Zone.ATTACHED;
    }

    public PhysicalCard getPlayedCard() {
        return _cardPlayed;
    }

    public PhysicalCard getAttachedTo() {
        return _attachedToCard;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return "Play card " + _cardPlayed.getBlueprint().getName();
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (_cardPlayed.getZone() != null)
            game.getGameState().removeCardsFromZone(_cardPlayed.getOwner(), Collections.singleton(_cardPlayed));
        if (_attachedToCard != null) {
            game.getGameState().attachCard(game, _cardPlayed, _attachedToCard);
        } else {
            game.getGameState().addCardToZone(game, _cardPlayed, _zone);
        }

        return new FullEffectResult(Collections.singleton(new PlayCardResult(_cardPlayed, _attachedToCard)), true, true);
    }
}
