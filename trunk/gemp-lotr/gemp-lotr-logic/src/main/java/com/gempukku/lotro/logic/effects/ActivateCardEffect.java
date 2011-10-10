package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

public class ActivateCardEffect extends AbstractEffect {
    private PhysicalCard _source;

    private boolean _cancelled;

    public ActivateCardEffect(PhysicalCard source) {
        _source = source;
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.ACTIVATE;
    }

    @Override
    public String getText(LotroGame game) {
        return "Activated " + GameUtils.getCardLink(_source);
    }

    public PhysicalCard getSource() {
        return _source;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }

    public boolean isCancelled() {
        return _cancelled;
    }

    public void cancel() {
        _cancelled = true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        return new FullEffectResult(null, true, true);
    }
}
