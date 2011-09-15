package com.gempukku.lotro.game;

import com.gempukku.lotro.logic.vo.LotroDeck;

public interface LotroFormat {
    public boolean isOrderedSites();

    public boolean validateDeck(LotroDeck deck);
}
