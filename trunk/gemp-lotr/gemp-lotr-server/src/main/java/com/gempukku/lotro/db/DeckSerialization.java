package com.gempukku.lotro.db;

import com.gempukku.lotro.logic.vo.LotroDeck;

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
        for (int i = 0; i < deck.getAdventureCards().size(); i++) {
            if (i > 0)
                sb.append(",");
            sb.append(deck.getAdventureCards().get(i));
        }

        return sb.toString();
    }

    public static LotroDeck buildDeckFromContents(String deckName, String contents) {
        // New format
        String[] parts = contents.split("\\|");

        LotroDeck deck = new LotroDeck(deckName);
        if (parts.length > 0 && !parts[0].equals(""))
            deck.setRingBearer(parts[0]);
        if (parts.length > 1 && !parts[1].equals(""))
            deck.setRing(parts[1]);
        if (parts.length > 2)
            for (String site : parts[2].split(",")) {
                if (!site.equals(""))
                    deck.addSite(site);
            }
        if (parts.length > 3)
            for (String card : parts[3].split(",")) {
                if (!card.equals(""))
                    deck.addCard(card);
            }

        return deck;
    }
}
