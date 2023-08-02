package com.gempukku.lotro.effects.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.effects.EffectResult;

public class ActivateCardResult extends EffectResult {
    private final LotroPhysicalCard _source;
    private final Phase _actionTimeword;
    private boolean _effectCancelled;

    public ActivateCardResult(LotroPhysicalCard source, Phase actionTimeword) {
        super(Type.ACTIVATE);
        _source = source;
        _actionTimeword = actionTimeword;
    }

    public Phase getActionTimeword() {
        return _actionTimeword;
    }

    public LotroPhysicalCard getSource() {
        return _source;
    }

    public void cancelEffect() {
        _effectCancelled = true;
    }

    public boolean isEffectCancelled() {
        return _effectCancelled;
    }
}
