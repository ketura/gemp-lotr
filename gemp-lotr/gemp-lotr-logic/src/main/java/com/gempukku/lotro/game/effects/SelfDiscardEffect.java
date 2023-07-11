package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.PhysicalCard;

public class SelfDiscardEffect extends DiscardCardsFromPlayEffect {
    public SelfDiscardEffect(PhysicalCard source) {
        super(source.getOwner(), source, source);
    }
}
