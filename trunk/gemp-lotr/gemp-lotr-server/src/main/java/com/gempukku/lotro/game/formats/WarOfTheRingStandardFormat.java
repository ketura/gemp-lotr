package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;

public class WarOfTheRingStandardFormat extends DefaultLotroFormat {
    public WarOfTheRingStandardFormat(LotroCardBlueprintLibrary library) {
        super(library, "War of the Ring standard", Block.OTHER, true, 60, 4, true, false);
        addValidSet(4);
        addValidSet(5);
        addValidSet(6);
        addValidSet(7);
        addValidSet(8);
        addValidSet(9);
        addValidSet(10);
        addValidSet(11);
        addValidSet(12);
        addValidSet(13);
        addValidSet(14);

        addBannedCard("4_73");
        addBannedCard("4_276");
        addBannedCard("4_304");
        addBannedCard("7_49");
        addBannedCard("8_1");
        addBannedCard("10_2");
        addBannedCard("10_11");
        addBannedCard("10_91");
        addBannedCard("11_31");
        addBannedCard("11_100");
        addBannedCard("11_132");
    }
}
