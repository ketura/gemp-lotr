package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.actions.Action;

public class SelfExertEffect extends ExertCharactersEffect {
    public SelfExertEffect(Action action, PhysicalCard source) {
        super(action, source, source);
    }
}
