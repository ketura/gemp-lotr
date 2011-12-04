package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.Action;

public class ExertResult extends EffectResult {
    private Action _action;
    private PhysicalCard _card;

    public ExertResult(Action action, PhysicalCard card) {
        super(Type.FOR_EACH_EXERTED);
        _action = action;
        _card = card;
    }

    public PhysicalCard getExertedCard() {
        return _card;
    }

    public Action getAction() {
        return _action;
    }
}
