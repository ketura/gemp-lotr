package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;

public class SelfDiscardEffect extends DiscardCardsFromPlayEffect {
    public SelfDiscardEffect(LotroPhysicalCard source) {
        super(source.getOwner(), source, source);
    }
}
