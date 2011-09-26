package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collection;
import java.util.Collections;

public class CardAffectsCardEffect extends UnrespondableEffect {
    private PhysicalCard _card;
    private Collection<PhysicalCard> _affectedCards;

    public CardAffectsCardEffect(PhysicalCard card, PhysicalCard affectedCard) {
        this(card, Collections.singleton(affectedCard));
    }

    public CardAffectsCardEffect(PhysicalCard card, Collection<PhysicalCard> affectedCards) {
        _card = card;
        _affectedCards = affectedCards;
    }

    @Override
    protected void doPlayEffect(LotroGame game) {
        game.getGameState().cardAffectsCard(_card, _affectedCards);
    }
}
