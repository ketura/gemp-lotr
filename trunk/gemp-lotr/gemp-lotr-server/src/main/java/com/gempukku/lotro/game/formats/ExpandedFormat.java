package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;

public class ExpandedFormat extends DefaultLotroFormat {
    public ExpandedFormat(LotroCardBlueprintLibrary library) {
        super(library, "Expanded", Block.OTHER, true, 60, 4, true, false);
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

        addBannedCard("1_45");
        addBannedCard("1_138");
        addBannedCard("1_234");
        addBannedCard("1_311");
        addBannedCard("1_313");
        addBannedCard("1_316");
        addBannedCard("2_121");
        addBannedCard("3_17");
        addBannedCard("3_38");
        addBannedCard("3_42");
        addBannedCard("3_67");
        addBannedCard("3_68");
        addBannedCard("3_108");
        addBannedCard("3_113");
        addBannedCard("4_73");
        addBannedCard("7_49");
        addBannedCard("8_1");
        addBannedCard("10_2");
        addBannedCard("10_11");
        addBannedCard("10_91");
        addBannedCard("11_31");
        addBannedCard("11_100");
        addBannedCard("11_132");

        addRestrictedCard("1_40");
        addRestrictedCard("1_80");
        addRestrictedCard("1_108");
        addRestrictedCard("1_139");
        addRestrictedCard("1_195");
        addRestrictedCard("1_248");
        addRestrictedCard("2_32");
        addRestrictedCard("2_75");
        addRestrictedCard("3_106");
        addRestrictedCard("4_276");
        addRestrictedCard("4_304");
    }
}
