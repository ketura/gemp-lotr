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
    public CardDeck(String deckName, String contents, String targetFormat, String notes) {
        // Assumes "new format" of LotR Gemp syntax
        String[] parts = contents.split("\\|");
        _deckName = deckName;
        _targetFormat = targetFormat;
        _notes = notes;

        if (parts.length > 0 && !parts[0].equals(""))
            this.addCard(parts[0]);
        if (parts.length > 1 && !parts[1].equals(""))
            this.addCard(parts[1]);
        if (parts.length > 2)
            for (String card : parts[2].split(",")) {
                if (!card.equals(""))
                    this.addCard(card);
            }
        if (parts.length > 3)
            for (String card : parts[3].split(",")) {
                if (!card.equals(""))
                    this.addCard(card);
            }
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
