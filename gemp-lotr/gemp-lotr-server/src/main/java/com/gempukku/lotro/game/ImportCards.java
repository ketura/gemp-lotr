package com.gempukku.lotro.game;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.logic.GameUtils;

import java.util.*;

public class ImportCards {
    //For a deck to be legal in a Pre-shadows format, it must contain one of these sites
    private Set<String> fellowshipSiteCheck = new HashSet<>(Arrays.asList("Council Courtyard",
            "Ford of Bruinen", "Frodo's Bedroom", "Rivendell Terrace", "Rivendell Valley", "Rivendell Waterfall",
            "House of Elrond"));
    private Set<String> towersSiteCheck = new HashSet<>(Arrays.asList("Derndingle", "Eastfold",
            "Fangorn Forest", "Plains of Rohan Camp", "Rohirrim Village", "Uruk Camp", "Wold of Rohan"));
    private Set<String> kingSiteCheck = new HashSet<>(Arrays.asList("King's Tent", "Rohirrim Camp",
            "West Road"));

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
        return Integer.parseInt(id.split("_")[0]) < 20;
    }

    private List<CardCount> getDecklist(String rawDecklist) {
        List<CardCount> result = new ArrayList<>();
        for (String line : rawDecklist.split("~")) {
            if (line.length() == 0)
                continue;

            line = line.toLowerCase();
            int quantity;

            String cardLine;
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
