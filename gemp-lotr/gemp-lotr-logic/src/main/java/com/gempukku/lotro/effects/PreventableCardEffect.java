package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

import java.util.Collection;

public interface PreventableCardEffect {
    Collection<LotroPhysicalCard> getAffectedCardsMinusPrevented(DefaultGame game);

    void preventEffect(DefaultGame game, LotroPhysicalCard affectedCard);
}
