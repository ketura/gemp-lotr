package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.lotronly.SubAction;
import com.gempukku.lotro.actions.Action;

public class CheckPhaseLimitEffect extends UnrespondableEffect {
    private final Action _action;
    private final LotroPhysicalCard _card;
    private final String _limitPrefix;
    private final int _limit;
    private final Phase _phase;
    private final Effect _limitedEffect;

    public CheckPhaseLimitEffect(Action action, LotroPhysicalCard card, int limit, Effect limitedEffect) {
        this(action, card, limit, null, limitedEffect);
    }

    public CheckPhaseLimitEffect(Action action, LotroPhysicalCard card, int limit, Phase phase, Effect limitedEffect) {
        this(action, card, "", limit, phase, limitedEffect);
    }

    public CheckPhaseLimitEffect(Action action, LotroPhysicalCard card, String limitPrefix, int limit, Phase phase, Effect limitedEffect) {
        _card = card;
        _limitPrefix = limitPrefix;
        _limit = limit;
        _phase = phase;
        _limitedEffect = limitedEffect;
        _action = action;
    }

    @Override
    public void doPlayEffect(DefaultGame game) {
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
