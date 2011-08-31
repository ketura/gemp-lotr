package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class CheckLimitEffect extends UnrespondableEffect {
    private CostToEffectAction _action;
    private PhysicalCard _card;
    private int _limit;
    private Phase _phase;
    private boolean _cost;
    private Effect _limitedEffect;

    public CheckLimitEffect(CostToEffectAction action, PhysicalCard card, int limit, Phase phase, boolean cost, Effect limitedEffect) {
        _card = card;
        _limit = limit;
        _phase = phase;
        _limitedEffect = limitedEffect;
        _action = action;
        _cost = cost;
    }

    @Override
    public void playEffect(LotroGame game) {
        int count = game.getModifiersEnvironment().getUntilEndOfPhaseLimitCounter(_card, _phase).incrementCounter();
        if (count <= _limit) {
            if (_cost)
                _action.addCost(_limitedEffect);
            else
                _action.addEffect(_limitedEffect);
        }
    }
}
