package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;

public class SelfDiscardEffect extends DiscardCardsFromPlayEffect {
    public SelfDiscardEffect(PhysicalCard source) {
        super(source.getOwner(), source, source);
    }
}
