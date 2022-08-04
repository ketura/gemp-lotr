package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class CheckPhaseLimitPerPlayerEffect extends UnrespondableEffect {
    private final Action _action;
    private final PhysicalCard _card;
    private final String _limitPrefix;
    private final String _playerId;
    private final int _limit;
    private final Phase _phase;
    private final Effect _limitedEffect;

    public CheckPhaseLimitPerPlayerEffect(Action action, PhysicalCard card, int limit, Effect limitedEffect) {
        this(action, card, limit, null, limitedEffect);
    }

    public CheckPhaseLimitPerPlayerEffect(Action action, PhysicalCard card, int limit, Phase phase, Effect limitedEffect) {
        this(action, card, "", limit, phase, limitedEffect);
    }

    public CheckPhaseLimitPerPlayerEffect(Action action, PhysicalCard card, String limitPrefix, int limit, Phase phase, Effect limitedEffect) {
        this(action, card, limitPrefix, "", limit, phase, limitedEffect);
    }

    public CheckPhaseLimitPerPlayerEffect(Action action, PhysicalCard card, String limitPrefix, String playerId, int limit, Phase phase, Effect limitedEffect) {
        _card = card;
        _limitPrefix = limitPrefix;
        _playerId = playerId;
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

        int incrementedBy = game.getModifiersQuerying().getUntilEndOfPhaseLimitCounter(_card, _playerId + "-" + _limitPrefix, phase).incrementToLimit(_limit, 1);
        if (incrementedBy > 0) {
            SubAction subAction = new SubAction(_action);
            subAction.appendEffect(
                    _limitedEffect);
            game.getActionsEnvironment().addActionToStack(subAction);
        }
    }
}
