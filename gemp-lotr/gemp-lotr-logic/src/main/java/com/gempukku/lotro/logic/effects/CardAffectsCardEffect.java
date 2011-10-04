package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collection;
import java.util.Collections;

public class CardAffectsCardEffect extends UnrespondableEffect {
    private String _playerPerforming;
    private PhysicalCard _card;
    private Collection<PhysicalCard> _affectedCards;

    public CardAffectsCardEffect(String playerPerforming, PhysicalCard card, PhysicalCard affectedCard) {
        this(playerPerforming, card, Collections.singleton(affectedCard));
    }

    public CardAffectsCardEffect(String playerPerforming, PhysicalCard card, Collection<PhysicalCard> affectedCards) {
        _playerPerforming = playerPerforming;
        _card = card;
        _affectedCards = affectedCards;
    }

    @Override
    protected void doPlayEffect(LotroGame game) {
        game.getGameState().cardAffectsCard(_playerPerforming, _card, _affectedCards);
    }
}
