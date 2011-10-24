package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AbstractPreventableCardEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collection;
import java.util.Collections;

public class PreventCardEffect extends UnrespondableEffect {
    private AbstractPreventableCardEffect _effect;
    private Collection<PhysicalCard> _cards;

    public PreventCardEffect(AbstractPreventableCardEffect effect, PhysicalCard card) {
        this(effect, Collections.singleton(card));
    }

    public PreventCardEffect(AbstractPreventableCardEffect effect, Collection<PhysicalCard> cards) {
        _effect = effect;
        _cards = cards;
    }

    @Override
    protected void doPlayEffect(LotroGame game) {
        for (PhysicalCard card : _cards)
            _effect.preventEffect(card);
    }
}
