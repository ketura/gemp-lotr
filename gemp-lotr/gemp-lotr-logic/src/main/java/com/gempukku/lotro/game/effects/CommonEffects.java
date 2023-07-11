package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.RequiredTriggerAction;
import com.gempukku.lotro.game.timing.EffectResult;
import com.gempukku.lotro.game.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

public class CommonEffects {
    public static List<RequiredTriggerAction> getSelfDiscardAtStartOfRegroup(DefaultGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.REGROUP)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
