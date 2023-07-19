package com.gempukku.lotro.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardDeck {
    protected final List<String> _drawDeckCards = new ArrayList<>();
    protected String _deckName;
    protected String _notes;
    protected String _targetFormat;
    public CardDeck(String deckName) {
        _deckName = deckName;
    }
    public String getDeckName() {
        return _deckName;
    }
    public void addCard(String card) {
        _drawDeckCards.add(card);
    }
    public List<String> getDrawDeckCards() {
        return Collections.unmodifiableList(_drawDeckCards);
    }
    public String getTargetFormat() { return _targetFormat; }
    public void setTargetFormat(String value) { _targetFormat = value; }
    public String getNotes() {
        return _notes;
    }
    public void setNotes(String value) {
        _notes = value;
    }
}
