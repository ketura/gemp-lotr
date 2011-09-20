package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

public class PlayCardEffect extends AbstractEffect {
    private PhysicalCard _cardPlayed;
    private PhysicalCard _attachedToCard;

    public PlayCardEffect(PhysicalCard cardPlayed) {
        _cardPlayed = cardPlayed;
    }

    public PlayCardEffect(PhysicalCard cardPlayed, PhysicalCard attachedToCard) {
        _cardPlayed = cardPlayed;
        _attachedToCard = attachedToCard;
    }

    public PhysicalCard getPlayedCard() {
        return _cardPlayed;
    }

    public PhysicalCard getAttachedTo() {
        return _attachedToCard;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return true;
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.PLAY;
    }

    @Override
    public String getText(LotroGame game) {
        return "Play card " + _cardPlayed.getBlueprint().getName();
    }

    @Override
    public EffectResult playEffect(LotroGame game) {
        return new PlayCardResult(_cardPlayed, _attachedToCard);
    }
}
