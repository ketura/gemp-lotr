package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;

public class WarOfTheRingBlockFormat extends DefaultLotroFormat {
    public WarOfTheRingBlockFormat(LotroCardBlueprintLibrary library) {
        super(library, "War of the Ring block", Block.OTHER, true, 60, 4, true, false);
        addRestrictedCard("11_132");
        addRestrictedCard("11_100");
        addValidSet(11);
        addValidSet(12);
        addValidSet(13);
    }
}
