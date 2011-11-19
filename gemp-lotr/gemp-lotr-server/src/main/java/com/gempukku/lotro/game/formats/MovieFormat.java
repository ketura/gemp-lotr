package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;

public class MovieFormat extends DefaultLotroFormat {
    public MovieFormat(LotroCardBlueprintLibrary library) {
        super(library, Block.KING, true, 60, 4, true);
        addBannedCard("8_1");
        addBannedCard("3_38");
        addBannedCard("3_106");
        addBannedCard("1_40");
        addBannedCard("2_32");
        addBannedCard("1_248");
        addBannedCard("3_108");
        addBannedCard("1_45");
        addBannedCard("7_96");
        addBannedCard("3_42");
        addBannedCard("10_2");
        addBannedCard("10_91");
        addBannedCard("1_108");
        addBannedCard("1_80");
        addBannedCard("3_67");
        addBannedCard("1_195");
        addBannedCard("3_68");
        addBannedCard("1_139");
        addBannedCard("7_49");
        addBannedCard("1_313");
        addBannedCard("1_234");


        addValidSet(1);
        addValidSet(2);
        addValidSet(3);
        addValidSet(4);
        addValidSet(5);
        addValidSet(6);
        addValidSet(7);
        addValidSet(8);
        addValidSet(9);
        addValidSet(10);
    }

    @Override
    public boolean isOrderedSites() {
        return true;
    }
}
