package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.Action;

public class SelfExertEffect extends ExertCharactersEffect {
    public SelfExertEffect(Action action, PhysicalCard source) {
        super(action, source, source);
    }
}
