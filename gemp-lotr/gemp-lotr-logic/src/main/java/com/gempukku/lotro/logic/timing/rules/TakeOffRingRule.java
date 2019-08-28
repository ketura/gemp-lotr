package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.TakeOffTheOneRingEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

public class TakeOffRingRule {
    private DefaultActionsEnvironment actionsEnvironment;

    public TakeOffRingRule(DefaultActionsEnvironment actionsEnvironment) {

        this.actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                        if ((TriggerConditions.startOfPhase(game, effectResult, Phase.REGROUP) || TriggerConditions.endOfPhase(game, effectResult, Phase.REGROUP))
                                && game.getGameState().isWearingRing()) {
                            final PhysicalCard ring = game.getGameState().getRing(game.getGameState().getCurrentPlayerId());
                            RequiredTriggerAction action = new RequiredTriggerAction(ring);
                            action.appendEffect(new TakeOffTheOneRingEffect());
                            return Collections.singletonList(action);
                        }
                        return null;
                    }
                });
    }
}
