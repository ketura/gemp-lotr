package com.gempukku.lotro.cards;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;

public class AbstractAdventure extends AbstractLotroCardBlueprint {
    public AbstractAdventure(String name, String subTitle) {
        super(0, null, CardType.ADVENTURE, null, name, subTitle, true);
    }

    @Override
    public CostToEffectAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return null;
    }
}
