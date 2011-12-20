package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;

public class OpenFormat extends DefaultLotroFormat {
    public OpenFormat(LotroCardBlueprintLibrary library) {
        super(library, "Open", Block.OTHER, true, 60, 4, true, false);
        addValidSet(0);
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
        addValidSet(11);
        addValidSet(12);
        addValidSet(13);
        addValidSet(14);
        addValidSet(15);
        addValidSet(16);
        addValidSet(17);
        addValidSet(18);
        addValidSet(19);

        addRestrictedCard("1_248");
        addRestrictedCard("7_49");
        addRestrictedCard("10_2");
        addRestrictedCard("10_91");
        addRestrictedCard("11_100");
        addRestrictedCard("11_132");
    }

    @Override
    public boolean isOrderedSites() {
        return true;
    }
}
