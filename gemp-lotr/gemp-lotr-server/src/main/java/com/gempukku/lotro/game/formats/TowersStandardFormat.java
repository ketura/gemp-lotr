package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;

public class TowersStandardFormat extends DefaultLotroFormat {
    public TowersStandardFormat(LotroCardBlueprintLibrary library) {
        super(library, Block.TWO_TOWERS, true, 60, 4);

        addBannedCard("1_40");
        addBannedCard("1_45");
        addBannedCard("1_80");
        addBannedCard("1_108");
        addBannedCard("1_139");
        addBannedCard("1_234");
        addBannedCard("1_248");
        addBannedCard("1_313");
        addBannedCard("2_32");
        addBannedCard("2_101");
        addBannedCard("2_108");
        addBannedCard("3_38");
        addBannedCard("3_42");
        addBannedCard("3_68");
        addBannedCard("4_192");

        addValidSet(1);
        addValidSet(2);
        addValidSet(3);
        addValidSet(4);
        addValidSet(5);
        addValidSet(6);
    }

    @Override
    public boolean isOrderedSites() {
        return true;
    }
}
