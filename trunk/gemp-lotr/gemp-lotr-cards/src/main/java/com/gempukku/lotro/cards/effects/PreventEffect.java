package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AbstractPreventableCardEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class PreventEffect extends UnrespondableEffect {
    private AbstractPreventableCardEffect _effect;
    private PhysicalCard _card;

    public PreventEffect(AbstractPreventableCardEffect effect, PhysicalCard card) {
        _effect = effect;
        _card = card;
    }

    @Override
    protected void doPlayEffect(LotroGame game) {
        _effect.preventEffect(_card);
    }
}
