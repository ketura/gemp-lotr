package com.gempukku.lotro.actions;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.lotronly.RequiredTriggerAction;
import com.gempukku.lotro.effects.Effect;
import com.gempukku.lotro.effects.EffectResult;

import java.util.List;

public interface ActionProxy {
    List<? extends Action> getPhaseActions(String playerId, DefaultGame game);

    List<? extends Action> getOptionalBeforeActions(String playerId, DefaultGame game, Effect effect);

    List<? extends RequiredTriggerAction> getRequiredBeforeTriggers(DefaultGame game, Effect effect);

    List<? extends OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, DefaultGame game, Effect effect);

    List<? extends Action> getOptionalAfterActions(String playerId, DefaultGame game, EffectResult effectResult);

    List<? extends RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame game, EffectResult effectResult);

    List<? extends OptionalTriggerAction> getOptionalAfterTriggerActions(String playerId, DefaultGame game, EffectResult effectResult);
}
