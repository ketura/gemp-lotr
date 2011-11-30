package com.gempukku.lotro.game;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public abstract class AbstractActionProxy implements ActionProxy {
    private Action.ActionSource _actionSource;

    public AbstractActionProxy(PhysicalCard actionSource) {
        _actionSource = new Action.ActionSource(actionSource);
    }

    public AbstractActionProxy(String ruleName) {
        _actionSource = new Action.ActionSource(ruleName);
    }

    @Override
    public Action.ActionSource getActionSource() {
        return _actionSource;
    }

    @Override
    public List<? extends RequiredTriggerAction> getRequiredBeforeTriggers(LotroGame game, Effect effect) {
        return null;
    }

    @Override
    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
        return null;
    }

    @Override
    public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult) {
        return null;
    }
}
