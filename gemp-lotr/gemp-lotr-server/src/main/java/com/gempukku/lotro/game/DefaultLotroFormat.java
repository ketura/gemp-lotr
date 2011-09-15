package com.gempukku.lotro.game;

import com.gempukku.lotro.logic.vo.LotroDeck;

public class DefaultLotroFormat implements LotroFormat {
    private boolean _orderedSites;

    public DefaultLotroFormat(boolean orderedSites) {
        _orderedSites = orderedSites;
    }

    @Override
    public boolean isOrderedSites() {
        return _orderedSites;
    }

    @Override
    public boolean validateDeck(LotroDeck deck) {
        return (deck != null);
    }
}
