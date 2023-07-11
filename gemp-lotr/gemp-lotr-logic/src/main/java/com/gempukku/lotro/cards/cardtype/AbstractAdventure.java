package com.gempukku.lotro.cards.cardtype;

import com.gempukku.lotro.common.CardType;

public class AbstractAdventure extends AbstractLotroCardBlueprint {
    public AbstractAdventure(String name, String subTitle) {
        super(0, null, CardType.ADVENTURE, null, name, subTitle, true);
    }
}
