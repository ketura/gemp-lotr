package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class CheckPhaseLimitEffect extends UnrespondableEffect {
    private Action _action;
    private PhysicalCard _card;
    private String _limitPrefix;
    private int _limit;
    private Phase _phase;
    private Effect _limitedEffect;

    public CheckPhaseLimitEffect(Action action, PhysicalCard card, int limit, Effect limitedEffect) {
        this(action, card, limit, null, limitedEffect);
    }

    public CheckPhaseLimitEffect(Action action, PhysicalCard card, int limit, Phase phase, Effect limitedEffect) {
        this(action, card, "", limit, phase, limitedEffect);
    }

    public CheckPhaseLimitEffect(Action action, PhysicalCard card, String limitPrefix, int limit, Phase phase, Effect limitedEffect) {
        _card = card;
        _limitPrefix = limitPrefix;
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

        int incrementedBy = game.getModifiersQuerying().getUntilEndOfPhaseLimitCounter(_card, _limitPrefix, phase).incrementToLimit(_limit, 1);
        if (incrementedBy > 0) {
            SubAction subAction = new SubAction(_action);
            subAction.appendEffect(
                    _limitedEffect);
            game.getActionsEnvironment().addActionToStack(subAction);
        }
    }
}
