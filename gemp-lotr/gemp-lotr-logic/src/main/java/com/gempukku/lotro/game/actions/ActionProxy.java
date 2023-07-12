package com.gempukku.lotro.game.actions;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.OptionalTriggerAction;
import com.gempukku.lotro.game.actions.lotronly.RequiredTriggerAction;
import com.gempukku.lotro.game.effects.Effect;
import com.gempukku.lotro.game.effects.EffectResult;

import java.util.List;

public interface ActionProxy {
    List<? extends Action> getPhaseActions(String playerId, DefaultGame game);

    List<? extends Action> getOptionalBeforeActions(String playerId, DefaultGame game, Effect effect);

    List<? extends RequiredTriggerAction> getRequiredBeforeTriggers(DefaultGame game, Effect effect);

    List<? extends OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, DefaultGame game, Effect effect);

    List<? extends Action> getOptionalAfterActions(String playerId, DefaultGame game, EffectResult effectResult);

    List<? extends RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame game, EffectResult effectResult);

    List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, DefaultGame game, EffectResult effectResult);
}
