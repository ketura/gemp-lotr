package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class CheckTurnLimitEffect extends UnrespondableEffect {
    private Action _action;
    private PhysicalCard _card;
    private int _limit;
    private Effect _limitedEffect;

    public CheckTurnLimitEffect(Action action, PhysicalCard card, int limit, Effect limitedEffect) {
        _card = card;
        _limit = limit;
        _limitedEffect = limitedEffect;
        _action = action;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        int incrementedBy = game.getModifiersQuerying().getUntilEndOfTurnLimitCounter(_card).incrementToLimit(_limit, 1);
        if (incrementedBy > 0) {
            SubAction subAction = new SubAction(_action);
            subAction.appendEffect(
                    _limitedEffect);
            game.getActionsEnvironment().addActionToStack(subAction);
        }
    }
}