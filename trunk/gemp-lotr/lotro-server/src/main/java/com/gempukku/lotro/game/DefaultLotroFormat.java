package com.gempukku.lotro.game;

public class DefaultLotroFormat implements LotroFormat {
    private boolean _orderedSites;

    public DefaultLotroFormat(boolean orderedSites) {
        _orderedSites = orderedSites;
    }

    @Override
    public boolean isOrderedSites() {
        return _orderedSites;
    }
}
