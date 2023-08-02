package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.actions.Action;

public class SelfExertEffect extends ExertCharactersEffect {
    public SelfExertEffect(Action action, LotroPhysicalCard source) {
        super(action, source, source);
    }
}
