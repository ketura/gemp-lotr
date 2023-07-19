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
        _targetFormat = "Anything Goes";
    }

    public LotroDeck(String deckName, String contents, String targetFormat, String notes) {
        super(deckName, contents, targetFormat, notes);
        // New format
        String[] parts = contents.split("\\|");
        this.emptyDrawDeck(); // Reverses default constructor actions

        if (parts.length > 0 && !parts[0].equals(""))
            _ringBearer = parts[0];
        if (parts.length > 1 && !parts[1].equals(""))
            _ring = parts[1];
        if (parts.length > 2)
            for (String site : parts[2].split(",")) {
                if (!site.equals(""))
                    this.addSite(site);
            }
        if (parts.length > 3)
            for (String card : parts[3].split(",")) {
                if (!card.equals(""))
                    this.addCard(card);
            }
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

    public void emptyDrawDeck() {
        _drawDeckCards.clear();
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

    public String buildContentsFromDeck() {
        StringBuilder sb = new StringBuilder();
        if (_ringBearer != null)
            sb.append(_ringBearer);
        sb.append("|");
        if (_ring != null)
            sb.append(_ring);
        sb.append("|");
        for (int i = 0; i < this.getSites().size(); i++) {
            if (i > 0)
                sb.append(",");
            sb.append(this.getSites().get(i));
        }
        sb.append("|");
        for (int i = 0; i < this.getDrawDeckCards().size(); i++) {
            if (i > 0)
                sb.append(",");
            sb.append(this.getDrawDeckCards().get(i));
        }

        return sb.toString();
    }
}
