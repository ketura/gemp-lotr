package com.gempukku.lotro.game;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.logic.GameUtils;

import java.util.*;
import java.util.regex.Pattern;

public class ImportCards {
    //For a deck to be legal in a Pre-shadows format, it must contain one of these sites
    private final Set<String> fellowshipSiteCheck = new HashSet<>(Arrays.asList("council courtyard",
            "ford of bruinen", "frodo's bedroom", "rivendell terrace", "rivendell valley", "rivendell waterfall",
            "house of elrond", "rivendell gateway"));
    private final Set<String> towersSiteCheck = new HashSet<>(Arrays.asList("derndingle", "eastfold",
            "fangorn forest", "plains of rohan camp", "rohirrim village", "uruk camp", "wold of rohan"));
    private final Set<String> kingSiteCheck = new HashSet<>(Arrays.asList("king's tent", "rohirrim camp", "west road"));

    public List<CardCollection.Item> process(String rawDecklist, LotroCardBlueprintLibrary cardLibrary) {
        List<CardCount> decklist = getDecklist(rawDecklist);
        SitesBlock sitesBlock = determineBlock(decklist);

        List<CardCollection.Item> result = new ArrayList<>();
        for (CardCount cardCount : decklist) {
            for (Map.Entry<String, LotroCardBlueprint> cardBlueprint : cardLibrary.getBaseCards().entrySet()) {
                String id = cardBlueprint.getKey();
                if (isFromSupportedSet(id)) {
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

    private boolean isFromSupportedSet(String id) {
        try {
            int set = Integer.parseInt(id.split("_")[0]);
            return set < 20 || (set > 99 && set < 149);
        } catch (NumberFormatException exp) {
            return false;
        }
    }

    private Pattern cardLinePattern = Pattern.compile("^(x?\\s*\\d+\\s*x?)?\\s*(.*)\\s*(x?\\d+x?)?\\s*$");

    private List<CardCount> getDecklist(String rawDecklist) {
        int quantity;
        String cardLine;

        List<CardCount> result = new ArrayList<>();
        for (String line : rawDecklist.split("~")) {
            if (line.length() == 0)
                continue;

            line = line.toLowerCase();
            try {
                var matches = cardLinePattern.matcher(line);

                if(matches.matches()) {
                    if(!matches.group(1).isBlank()) {
                        quantity = Integer.parseInt(matches.group(1).replaceAll("\\D+", ""));
                    }
                    else if(!matches.group(3).isBlank()) {
                        quantity = Integer.parseInt(matches.group(3).replaceAll("\\D+", ""));
                    }
                    else {
                        quantity = 1;
                    }

                    cardLine = matches.group(2).trim();
                    result.add(new CardCount(SortAndFilterCards.replaceSpecialCharacters(cardLine).trim(), quantity));
                }
            } catch (Exception exp) {
                // Ignore the card
            }
        }
        return result;
    }

    private static class CardCount {
        private final String name;
        private final int count;

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
