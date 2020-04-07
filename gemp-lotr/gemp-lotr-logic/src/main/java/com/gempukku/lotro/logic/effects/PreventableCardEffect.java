package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

import java.util.Collection;

public interface PreventableCardEffect {
    Collection<PhysicalCard> getAffectedCardsMinusPrevented(LotroGame game);

    void preventEffect(LotroGame game, PhysicalCard affectedCard);
}
