package com.gempukku.lotro.logic.vo;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LotroDeck {
    private String _ringBearer;
    private String _ring;
    private List<String> _siteCards = new LinkedList<String>();
    private List<String> _nonSiteCards = new LinkedList<String>();

    public void setRingBearer(String ringBearer) {
        _ringBearer = ringBearer;
    }

    public void setRing(String ring) {
        _ring = ring;
    }

    public void addCard(String card) {
        _nonSiteCards.add(card);
    }

    public void addSite(String card) {
        _siteCards.add(card);
    }

    public List<String> getAdventureCards() {
        return Collections.unmodifiableList(_nonSiteCards);
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
