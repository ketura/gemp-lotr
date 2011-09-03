package com.gempukku.lotro.db.vo;

import java.util.List;

public class Deck {
    private String _ringBearer;
    private String _ring;
    private List<String> _sites;
    private List<String> _deck;

    public Deck(String ringBearer, String ring, List<String> sites, List<String> deck) {
        _ringBearer = ringBearer;
        _ring = ring;
        _sites = sites;
        _deck = deck;
    }

    public String getRingBearer() {
        return _ringBearer;
    }

    public String getRing() {
        return _ring;
    }

    public List<String> getSites() {
        return _sites;
    }

    public List<String> getDeck() {
        return _deck;
    }
}
