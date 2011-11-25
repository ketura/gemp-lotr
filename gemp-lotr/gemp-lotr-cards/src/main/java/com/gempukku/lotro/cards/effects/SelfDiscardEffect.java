package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;

public class SelfDiscardEffect extends DiscardCardsFromPlayEffect {
    public SelfDiscardEffect(PhysicalCard source) {
        super(source, source);
    }
}
