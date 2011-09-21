package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class CardAffectsCardEffect extends UnrespondableEffect {
    private PhysicalCard _card;
    private PhysicalCard _affectedCard;

    public CardAffectsCardEffect(PhysicalCard card, PhysicalCard affectedCard) {
        _card = card;
        _affectedCard = affectedCard;
    }

    @Override
    protected void doPlayEffect(LotroGame game) {
        game.getGameState().cardAffectsCard(_card, _affectedCard);
    }
}
