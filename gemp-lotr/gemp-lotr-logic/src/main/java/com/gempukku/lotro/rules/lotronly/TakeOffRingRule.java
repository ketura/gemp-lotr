package com.gempukku.lotro.rules.lotronly;

import com.gempukku.lotro.actions.AbstractActionProxy;
import com.gempukku.lotro.actions.lotronly.RequiredTriggerAction;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.effects.TakeOffTheOneRingEffect;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.effects.EffectResult;
import com.gempukku.lotro.game.TriggerConditions;

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
                            final LotroPhysicalCard ring = game.getGameState().getRing(game.getGameState().getCurrentPlayerId());
                            RequiredTriggerAction action = new RequiredTriggerAction(ring);
                            action.appendEffect(new TakeOffTheOneRingEffect());
                            return Collections.singletonList(action);
                        }
                        return null;
                    }
                });
    }
}
