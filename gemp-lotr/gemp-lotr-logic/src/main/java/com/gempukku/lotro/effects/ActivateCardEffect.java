package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.effects.results.ActivateCardResult;

public class ActivateCardEffect extends AbstractEffect {
    private final LotroPhysicalCard _source;
    private final Phase _actionTimeword;

    private final ActivateCardResult _activateCardResult;

    public ActivateCardEffect(LotroPhysicalCard source, Phase actionTimeword) {
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
    public String getText(DefaultGame game) {
        return "Activated " + GameUtils.getCardLink(_source);
    }

    public LotroPhysicalCard getSource() {
        return _source;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        game.getActionsEnvironment().emitEffectResult(_activateCardResult);
        return new FullEffectResult(true);
    }
}
