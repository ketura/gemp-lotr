package com.gempukku.lotro.game;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.logic.GameUtils;

import java.util.*;

public class ImportCards {
    private SitesBlock _siteBlock;

    //For a deck to be legal in a Pre-shadows format, it must contain one of these sites
    private List<String> _fellowshipSiteCheck = new ArrayList<String>(Arrays.asList("Council Courtyard",
            "Ford of Bruinen", "Frodo's Bedroom", "Rivendell Terrace", "Rivendell Valley", "Rivendell Waterfall",
            "House of Elrond"));
    private List<String> _towersSiteCheck = new ArrayList<String>(Arrays.asList("Derndingle", "Eastfold",
            "Fangorn Forest", "Plains of Rohan Camp", "Rohirrim Village", "Uruk Camp", "Wold of Rohan"));
    private List<String> _kingSiteCheck = new ArrayList<String>(Arrays.asList("King's Tent", "Rohirrim Camp",
            "West Road"));
    
    public <T extends CardItem> List<T> process(String rawDecklist, Iterable<T> items, LotroCardBlueprintLibrary cardLibrary) {
        List<String> decklist = getDecklist(rawDecklist);
        _siteBlock = null;

        List<T> result = new ArrayList<T>();
        Set<T> pendingSites = new HashSet<T>();
        Map<String, LotroCardBlueprint> cardBlueprintMap = new HashMap<>();
        for (String line : decklist) {
            for (T item : items) {
                String blueprintId = item.getBlueprintId();
                if (isPack(blueprintId))
                    continue;
                try {
                    cardBlueprintMap.put(blueprintId, cardLibrary.getLotroCardBlueprint(blueprintId));
                    int outcome = importCriteria(cardLibrary, cardBlueprintMap, blueprintId, line);
                    if (outcome == 1) {
                       result.add(item);
                    }
                    if (outcome == 0)
                       pendingSites.add(item);
                } catch (CardNotFoundException e) {
                    // Ignore the card
                }
            }
        }

        if (_siteBlock == null)
            _siteBlock = SitesBlock.SHADOWS;
        for (T siteItem : pendingSites) {
            String siteId = siteItem.getBlueprintId();
            try {
                LotroCardBlueprint siteBlueprint = cardLibrary.getLotroCardBlueprint(siteId);
                if (siteBlockFilter(siteBlueprint))
                    result.add(siteItem);
            } catch (CardNotFoundException e) {
                // Ignore the card
            }
        }

        return result;
    }

    private int importCriteria(LotroCardBlueprintLibrary library, Map<String, LotroCardBlueprint> cardBlueprint, String blueprintId, String title) {
        final LotroCardBlueprint blueprint = cardBlueprint.get(blueprintId);
        if (exactMatch(blueprint,title)) {
            if (setFilter(blueprintId)) {
                if (blueprint.getCardType() == CardType.SITE) {
                    if (_siteBlock == null)
                        findSiteBlock(blueprint);
                    //add all sites to the set, as some formats would add duplicates otherwise
                    return 0; 
                }
                return 1;
            }
        }
        return -1;
    }

    private boolean exactMatch(LotroCardBlueprint blueprint, String title) {
        if (blueprint == null || !replaceSpecialCharacters(GameUtils.getFullName(blueprint).toLowerCase()).equals(title))
            return false;
        return true;
    }
    
    private boolean setFilter(String blueprintId) {
        try {
            int setNo = Integer.parseInt(blueprintId.split("_")[0]);
            if (setNo < 20)
                return true;
        } catch (Exception e) {
            //Not a card
        }
        return false;
    }

    private boolean siteBlockFilter(LotroCardBlueprint blueprint) {
        if (blueprint.getSiteBlock() == _siteBlock)
            return true;
        return false;
    }
    
    private void findSiteBlock(LotroCardBlueprint cardBlueprint) {
        String site = cardBlueprint.getTitle();
        if (_fellowshipSiteCheck.contains(site))
            _siteBlock = SitesBlock.FELLOWSHIP;
        else if (_towersSiteCheck.contains(site))
            _siteBlock = SitesBlock.TWO_TOWERS;
        else if (_kingSiteCheck.contains(site))
            _siteBlock = SitesBlock.KING;
    }
    
    private List<String> getDecklist(String rawDecklist) {
        List<String> result = new ArrayList<String>();
        for (String line : rawDecklist.split("~")) {
            if (line.length() == 0)
                continue;
            int quantity = 1;
            line = line.toLowerCase();
            String cardLine = "";
            if (Character.isDigit(line.charAt(0))) {
                quantity = Character.getNumericValue(line.charAt(0));
                cardLine = line.substring(line.indexOf(" "));
            }
            else if (Character.isDigit(line.charAt(line.length()-1))) {
                quantity = Character.getNumericValue(line.charAt(line.length()-1));
                cardLine = line.substring(0,line.indexOf(" ",line.length()-3));
            }
            else {
                quantity = 1;
                cardLine = line;
            }
            for (int i = 0; i < quantity; i++)
                result.add(replaceSpecialCharacters(cardLine).trim());
        }
        return result;
    }

    private String replaceSpecialCharacters(String text) {
        return text
                .replace('é', 'e')
                .replace('ú', 'u')
                .replace('ë', 'e')
                .replace('û', 'u')
                .replace('ó', 'o');
    }

    private static boolean isPack(String blueprintId) {
        return !blueprintId.contains("_");
    }
}
