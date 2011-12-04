package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collection;
import java.util.Collections;

public class NegateWoundEffect extends UnrespondableEffect {
    private WoundCharactersEffect _effect;
    private Collection<PhysicalCard> _cards;

    public NegateWoundEffect(WoundCharactersEffect effect, PhysicalCard card) {
        this(effect, Collections.singleton(card));
    }

    public NegateWoundEffect(WoundCharactersEffect effect, Collection<PhysicalCard> cards) {
        _effect = effect;
        _cards = cards;
    }

    @Override
    protected void doPlayEffect(LotroGame game) {
        for (PhysicalCard card : _cards)
            _effect.negateWound(game, card);
    }
}
