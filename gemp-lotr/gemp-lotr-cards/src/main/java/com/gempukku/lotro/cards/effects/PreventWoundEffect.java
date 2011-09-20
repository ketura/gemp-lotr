package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class PreventWoundEffect extends UnrespondableEffect {
    private WoundCharacterEffect _effect;
    private PhysicalCard _card;

    public PreventWoundEffect(WoundCharacterEffect effect, PhysicalCard card) {
        _effect = effect;
        _card = card;
    }

    @Override
    protected void doPlayEffect(LotroGame game) {
        _effect.prevent(_card);
    }
}
