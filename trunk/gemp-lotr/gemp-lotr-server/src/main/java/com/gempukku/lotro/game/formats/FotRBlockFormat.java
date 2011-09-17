package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.game.LotroCardBlueprintLibrary;

public class FotRBlockFormat extends DefaultLotroFormat {
    public FotRBlockFormat(LotroCardBlueprintLibrary library) {
        super(library, true, 60, 4);
        addRestrictedCard("Forces of Mordor");
    }

    @Override
    public boolean isOrderedSites() {
        return true;
    }
}
