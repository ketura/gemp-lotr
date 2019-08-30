package com.gempukku.lotro.game;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public interface ActionProxy {
    public List<? extends RequiredTriggerAction> getRequiredBeforeTriggers(LotroGame lotroGame, Effect effect);

    public List<? extends OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, LotroGame lotroGame, Effect effect);

    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame lotroGame, EffectResult effectResult);

    public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame lotroGame, EffectResult effectResult);
}
