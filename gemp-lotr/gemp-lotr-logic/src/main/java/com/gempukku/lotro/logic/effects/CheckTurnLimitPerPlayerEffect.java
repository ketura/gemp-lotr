package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubCostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class CheckTurnLimitPerPlayerEffect extends UnrespondableEffect {
    private Action _action;
    private PhysicalCard _card;
    private String _playerId;
    private int _limit;
    private Effect _limitedEffect;

    public CheckTurnLimitPerPlayerEffect(Action action, PhysicalCard card, String playerId, int limit, Effect limitedEffect) {
        _card = card;
        this._playerId = playerId;
        _limit = limit;
        _limitedEffect = limitedEffect;
        _action = action;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        int incrementedBy = game.getModifiersQuerying().getUntilEndOfTurnLimitCounter(_card, _playerId +"-").incrementToLimit(_limit, 1);
        if (incrementedBy > 0) {
            SubCostToEffectAction subAction = new SubCostToEffectAction(_action);
            subAction.appendEffect(
                    _limitedEffect);
            game.getActionsEnvironment().addActionToStack(subAction);
        }
    }
}