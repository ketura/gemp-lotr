package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;

public class SelfExertEffect extends ExertCharactersEffect {
    public SelfExertEffect(PhysicalCard source) {
        super(source, source);
    }
}
