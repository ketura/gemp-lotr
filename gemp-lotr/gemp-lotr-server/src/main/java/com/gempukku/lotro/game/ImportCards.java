package com.gempukku.lotro.game;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.logic.GameUtils;

import java.util.*;

public class ImportCards {
    //For a deck to be legal in a Pre-shadows format, it must contain one of these sites
    private Set<String> fellowshipSiteCheck = new HashSet<>(Arrays.asList("council courtyard",
            "ford of bruinen", "frodo's bedroom", "rivendell terrace", "rivendell valley", "rivendell waterfall",
            "house of elrond"));
    private Set<String> towersSiteCheck = new HashSet<>(Arrays.asList("derndingle", "eastfold",
            "fangorn forest", "plains of rohan camp", "rohirrim village", "uruk camp", "wold of rohan"));
    private Set<String> kingSiteCheck = new HashSet<>(Arrays.asList("king's tent", "rohirrim camp", "west road"));

    public List<CardCollection.Item> process(String rawDecklist, LotroCardBlueprintLibrary cardLibrary) {
        List<CardCount> decklist = getDecklist(rawDecklist);
        SitesBlock sitesBlock = determineBlock(decklist);

        List<CardCollection.Item> result = new ArrayList<>();
        for (CardCount cardCount : decklist) {
            for (Map.Entry<String, LotroCardBlueprint> cardBlueprint : cardLibrary.getBaseCards().entrySet()) {
                String id = cardBlueprint.getKey();
                if (isFromOfficialSet(id)) {
                    LotroCardBlueprint blueprint = cardBlueprint.getValue();
                    if (isNotSiteOrSiteFromBlock(blueprint, sitesBlock)) {
                        if (exactNameMatch(blueprint, cardCount.getName())) {
                            result.add(CardCollection.Item.createItem(id, cardCount.getCount()));
                            break;
                        }
                    }
                }
            }
        }

        return result;
    }

    private boolean isNotSiteOrSiteFromBlock(LotroCardBlueprint blueprint, SitesBlock sitesBlock) {
        return blueprint.getCardType() != CardType.SITE || blueprint.getSiteBlock() == sitesBlock;
    }

    private SitesBlock determineBlock(List<CardCount> decklist) {
        for (CardCount card : decklist) {
            String name = card.getName();
            if (fellowshipSiteCheck.contains(name))
                return SitesBlock.FELLOWSHIP;
            if (towersSiteCheck.contains(name))
                return SitesBlock.TWO_TOWERS;
            if (kingSiteCheck.contains(name))
                return SitesBlock.KING;
        }

        return SitesBlock.SHADOWS;
    }

    private boolean exactNameMatch(LotroCardBlueprint blueprint, String title) {
        return blueprint != null
                && SortAndFilterCards.replaceSpecialCharacters(GameUtils.getFullName(blueprint).toLowerCase()).equals(title);
    }

    private boolean isFromOfficialSet(String id) {
        try {
            return Integer.parseInt(id.split("_")[0]) < 20;
        } catch (NumberFormatException exp) {
            return false;
        }
    }

    private List<CardCount> getDecklist(String rawDecklist) {
        int quantity;
        String cardLine;

        List<CardCount> result = new ArrayList<>();
        for (String line : rawDecklist.split("~")) {
            if (line.length() == 0)
                continue;

            line = line.toLowerCase();
            try {
                if (Character.isDigit(line.charAt(0))) {
                    quantity = Character.getNumericValue(line.charAt(0));
                    cardLine = line.substring(line.indexOf(" "));
                } else if (Character.isDigit(line.charAt(line.length() - 1))) {
                    quantity = Character.getNumericValue(line.charAt(line.length() - 1));
                    cardLine = line.substring(0, line.indexOf(" ", line.length() - 3));
                } else {
                    quantity = 1;
                    cardLine = line;
                }
                result.add(new CardCount(SortAndFilterCards.replaceSpecialCharacters(cardLine).trim(), quantity));
            } catch (Exception exp) {
                // Ignore the card
            }
        }
        return result;
    }

    private static class CardCount {
        private String name;
        private int count;

        public CardCount(String name, int count) {
            this.name = name;
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public int getCount() {
            return count;
        }
    }
}
