package com.gempukku.lotro.db.vo;

import java.util.Collections;
import java.util.List;

public class DeckVO {
    private String _ringBearer;
    private String _ring;
    private List<String> _cards;

    public DeckVO(String ringBearer, String ring, List<String> cards) {
        _ringBearer = ringBearer;
        _ring = ring;
        _cards = cards;
    }

    public List<String> getCards() {
        return Collections.unmodifiableList(_cards);
    }

    public String getRing() {
        return _ring;
    }

    public String getRingBearer() {
        return _ringBearer;
    }
}
