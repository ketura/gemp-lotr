package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.ActivateCardResult;

public class ActivateCardEffect extends AbstractEffect {
    private PhysicalCard _source;
    private Phase _actionTimeword;

    private ActivateCardResult _activateCardResult;

    public ActivateCardEffect(PhysicalCard source, Phase actionTimeword) {
        _source = source;
        _actionTimeword = actionTimeword;

        _activateCardResult = new ActivateCardResult(_source, _actionTimeword);
    }

    public ActivateCardResult getActivateCardResult() {
        return _activateCardResult;
    }

    public Phase getActionTimeword() {
        return _actionTimeword;
    }

    @Override
    public Effect.Type getType() {
        return null;
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

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        return new FullEffectResult(new EffectResult[]{_activateCardResult}, true, true);
    }
}
