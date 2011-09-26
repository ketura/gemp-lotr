package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class CheckLimitEffect extends UnrespondableEffect {
    private ActivateCardAction _action;
    private PhysicalCard _card;
    private int _limit;
    private Phase _phase;
    private Effect _limitedEffect;

    public CheckLimitEffect(ActivateCardAction action, PhysicalCard card, int limit, Phase phase, Effect limitedEffect) {
        _card = card;
        _limit = limit;
        _phase = phase;
        _limitedEffect = limitedEffect;
        _action = action;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        int count = game.getModifiersEnvironment().getUntilEndOfPhaseLimitCounter(_card, _phase).incrementCounter();
        if (count <= _limit) {
            _action.appendEffect(_limitedEffect);
        }
    }
}
