package com.gempukku.lotro.game.rules.lotronly;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.AbstractActionProxy;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.game.actions.lotronly.RequiredTriggerAction;
import com.gempukku.lotro.game.effects.TakeOffTheOneRingEffect;
import com.gempukku.lotro.game.effects.EffectResult;
import com.gempukku.lotro.game.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

public class TakeOffRingRule {
    private final DefaultActionsEnvironment actionsEnvironment;

    public TakeOffRingRule(DefaultActionsEnvironment actionsEnvironment) {

        this.actionsEnvironment = actionsEnvironment;
    }

    public void applyRule() {
        actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame game, EffectResult effectResult) {
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
