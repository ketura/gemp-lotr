package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.AbstractCostToEffectAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import com.gempukku.lotro.logic.timing.Action;

public class CheckLimitEffect extends UnrespondableEffect {
    private Action _action;
    private PhysicalCard _card;
    private int _limit;
    private Phase _phase;
    private Effect _limitedEffect;

    public CheckLimitEffect(Action action, PhysicalCard card, int limit, Effect limitedEffect) {
        this(action, card, limit, null, limitedEffect);
    }

    public CheckLimitEffect(Action action, PhysicalCard card, int limit, Phase phase, Effect limitedEffect) {
        _card = card;
        _limit = limit;
        _phase = phase;
        _limitedEffect = limitedEffect;
        _action = action;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        Phase phase = _phase;
        if (phase == null)
            phase = game.getGameState().getCurrentPhase();
        
        int count = game.getModifiersEnvironment().getUntilEndOfPhaseLimitCounter(_card, phase).incrementCounter();
        if (count <= _limit) {
            SubAction subAction = new SubAction(_action);
            subAction.appendEffect(
                    _limitedEffect);
            game.getActionsEnvironment().addActionToStack(subAction);
        }
    }
}
