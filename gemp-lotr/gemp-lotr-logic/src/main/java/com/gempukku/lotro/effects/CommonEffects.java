package com.gempukku.lotro.effects;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.lotronly.RequiredTriggerAction;
import com.gempukku.lotro.game.TriggerConditions;

import java.util.Collections;
import java.util.List;

public class CommonEffects {
    public static List<RequiredTriggerAction> getSelfDiscardAtStartOfRegroup(DefaultGame game, EffectResult effectResult, LotroPhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.REGROUP)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
