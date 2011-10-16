package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;

public class TTTBlockFormat extends DefaultLotroFormat {
    public TTTBlockFormat(LotroCardBlueprintLibrary library) {
        super(library, Block.TWO_TOWERS, true, 60, 4);
        addValidSet(4);
        addValidSet(5);
        addValidSet(6);
    }

    @Override
    public boolean isOrderedSites() {
        return true;
    }
}
