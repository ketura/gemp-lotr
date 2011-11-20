package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;

public class FreeFormat extends DefaultLotroFormat {
    public FreeFormat(LotroCardBlueprintLibrary library) {
        super(library, Block.FELLOWSHIP, false, 0, Integer.MAX_VALUE, false, true);
    }

    @Override
    public boolean isOrderedSites() {
        return true;
    }
}
