package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.game.DeckInvalidException;
import com.gempukku.lotro.logic.vo.LotroDeck;

public interface LotroFormat {
    public boolean isOrderedSites();

    public void validateDeck(LotroDeck deck) throws DeckInvalidException;
}
