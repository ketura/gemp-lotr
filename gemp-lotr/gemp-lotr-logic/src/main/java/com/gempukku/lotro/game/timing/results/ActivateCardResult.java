package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.effects.EffectResult;

public class ActivateCardResult extends EffectResult {
    private final PhysicalCard _source;
    private final Phase _actionTimeword;
    private boolean _effectCancelled;

    public ActivateCardResult(PhysicalCard source, Phase actionTimeword) {
        super(Type.ACTIVATE);
        _source = source;
        _actionTimeword = actionTimeword;
    }

    public Phase getActionTimeword() {
        return _actionTimeword;
    }

    public PhysicalCard getSource() {
        return _source;
    }

    public void cancelEffect() {
        _effectCancelled = true;
    }

    public boolean isEffectCancelled() {
        return _effectCancelled;
    }
}
