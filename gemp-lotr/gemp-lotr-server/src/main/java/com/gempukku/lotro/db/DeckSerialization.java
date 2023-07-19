package com.gempukku.lotro.db;

import com.gempukku.lotro.cards.lotronly.LotroDeck;

public class DeckSerialization {
    public static String buildContentsFromDeck(LotroDeck deck) {
        StringBuilder sb = new StringBuilder();
        if (deck.getRingBearer() != null)
            sb.append(deck.getRingBearer());
        sb.append("|");
        if (deck.getRing() != null)
            sb.append(deck.getRing());
        sb.append("|");
        for (int i = 0; i < deck.getSites().size(); i++) {
            if (i > 0)
                sb.append(",");
            sb.append(deck.getSites().get(i));
        }
        sb.append("|");
        for (int i = 0; i < deck.getDrawDeckCards().size(); i++) {
            if (i > 0)
                sb.append(",");
            sb.append(deck.getDrawDeckCards().get(i));
        }

        return sb.toString();
    }

    public static LotroDeck buildDeckFromContents(String deckName, String contents, String targetFormat, String notes) {
        return new LotroDeck(deckName, contents, targetFormat, notes);
    }
}
