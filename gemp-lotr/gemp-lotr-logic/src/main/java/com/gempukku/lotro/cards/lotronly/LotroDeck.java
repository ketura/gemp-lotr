package com.gempukku.lotro.cards.lotronly;

import com.gempukku.lotro.cards.CardDeck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LotroDeck extends CardDeck {
    private String _ringBearer;
    private String _ring;
    private final List<String> _siteCards = new ArrayList<>();

    public LotroDeck(String deckName) {
        super(deckName);
        _deckName = deckName;
        _targetFormat = "Anything Goes";
    }

    public void setRingBearer(String ringBearer) {
        _ringBearer = ringBearer;
    }

    public void setRing(String ring) {
        _ring = ring;
    }

    public void addSite(String card) {
        _siteCards.add(card);
    }

    public List<String> getSites() {
        return Collections.unmodifiableList(_siteCards);
    }

    public String getRingBearer() {
        return _ringBearer;
    }

    public String getRing() {
        return _ring;
    }
}
