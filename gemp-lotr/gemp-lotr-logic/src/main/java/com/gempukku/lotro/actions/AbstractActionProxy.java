package com.gempukku.lotro.actions;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.lotronly.RequiredTriggerAction;
import com.gempukku.lotro.effects.Effect;
import com.gempukku.lotro.effects.EffectResult;

import java.util.List;

public abstract class AbstractActionProxy implements ActionProxy {
    @Override
    public List<? extends Action> getPhaseActions(String playerId, DefaultGame game) {
        return null;
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, DefaultGame game, Effect effect) {
        return null;
    }

    @Override
    public List<? extends Action> getOptionalAfterActions(String playerId, DefaultGame game, EffectResult effectResult) {
        return null;
    }

    @Override
    public List<? extends RequiredTriggerAction> getRequiredBeforeTriggers(DefaultGame game, Effect effect) {
        return null;
    }

    @Override
    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame game, EffectResult effectResult) {
        return null;
    }

    @Override
    public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, DefaultGame game, EffectResult effectResult) {
        return null;
    }

    @Override
    public List<? extends OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, DefaultGame game, Effect effect) {
        return null;
    }
}
