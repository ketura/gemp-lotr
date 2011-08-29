package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.effects.WoundEffect;

public class WoundAction extends CostToEffectAction {
    public WoundAction(PhysicalCard card, int wounds) {
        super(null, "Wound character");
        for (int i = 0; i < wounds; i++)
            addEffect(new WoundEffect(card));
    }
}
