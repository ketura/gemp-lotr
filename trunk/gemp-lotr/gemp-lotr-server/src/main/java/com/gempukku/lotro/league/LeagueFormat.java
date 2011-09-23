package com.gempukku.lotro.league;

import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.formats.DefaultLotroFormat;

public class LeagueFormat extends DefaultLotroFormat {
    private boolean _orderedSites;

    public LeagueFormat(LotroCardBlueprintLibrary library, boolean orderedSites) {
        super(library, true, 60, Integer.MAX_VALUE);
        _orderedSites = orderedSites;
    }

    @Override
    public boolean isOrderedSites() {
        return _orderedSites;
    }
}
