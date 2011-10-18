package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;

public class FotRBlockFormat extends DefaultLotroFormat {
    public FotRBlockFormat(LotroCardBlueprintLibrary library, boolean mulliganRule) {
        super(library, Block.FELLOWSHIP, true, 60, 4, mulliganRule);
        addRestrictedCard("1_248");
        addValidSet(1);
        addValidSet(2);
        addValidSet(3);
    }

    @Override
    public boolean isOrderedSites() {
        return true;
    }
}
