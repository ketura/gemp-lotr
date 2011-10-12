package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;

public class RemoveCardFromZoneEffect extends UnrespondableEffect {
    private PhysicalCard _physicalCard;

    public RemoveCardFromZoneEffect(PhysicalCard physicalCard) {
        _physicalCard = physicalCard;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        game.getGameState().removeCardsFromZone(Collections.singleton(_physicalCard));
    }
}