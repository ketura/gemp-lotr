package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.game.LotroCardBlueprintLibrary;

public class ModifiedFotRBlockFormat extends DefaultLotroFormat {
    public ModifiedFotRBlockFormat(LotroCardBlueprintLibrary library) {
        super(library, false, 0, Integer.MAX_VALUE);
    }

    @Override
    public boolean isOrderedSites() {
        return true;
    }
}
