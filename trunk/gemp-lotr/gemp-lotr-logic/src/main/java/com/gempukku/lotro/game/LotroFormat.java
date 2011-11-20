package com.gempukku.lotro.game;

import com.gempukku.lotro.logic.vo.LotroDeck;

public interface LotroFormat {
    public boolean isOrderedSites();

    public boolean canCancelRingBearerSkirmish();

    public boolean hasMulliganRule();

    public void validateDeck(LotroDeck deck) throws DeckInvalidException;
}
