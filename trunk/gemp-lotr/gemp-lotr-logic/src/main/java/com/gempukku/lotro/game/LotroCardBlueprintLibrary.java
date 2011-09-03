package com.gempukku.lotro.game;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class LotroCardBlueprintLibrary {
    private String[] _packageNames =
            new String[]{
                    "", ".dwarven", ".elven", ".isengard", ".moria", ".shire", ".site"
            };
    private Map<String, LotroCardBlueprint> _blueprintMap = new TreeMap<String, LotroCardBlueprint>();

    public LotroCardBlueprint getLotroCardBlueprint(String blueprintId) {
        if (_blueprintMap.containsKey(blueprintId))
            return _blueprintMap.get(blueprintId);

        LotroCardBlueprint blueprint = getBlueprint(blueprintId);
        _blueprintMap.put(blueprintId, blueprint);
        return blueprint;
    }

    public void initializeLibrary(String setNo, int maxCardIndex) {
        for (int i = 1; i <= maxCardIndex; i++) {
            try {
                getLotroCardBlueprint(setNo + "_" + i);
            } catch (IllegalArgumentException exp) {
                // Ignore
            }
        }
    }

    public Collection<LotroCardBlueprint> getAllLoadedBlueprints() {
        return _blueprintMap.values();
    }

    private LotroCardBlueprint getBlueprint(String blueprintId) {
        String[] blueprintParts = blueprintId.split("_");

        String setNumber = blueprintParts[0];
        String cardNumber = blueprintParts[1];

        for (String packageName : _packageNames) {
            LotroCardBlueprint blueprint = null;
            try {
                blueprint = tryLoadingFromPackage(packageName, setNumber, cardNumber);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            if (blueprint != null)
                return blueprint;
        }

        throw new IllegalArgumentException("Didn't find card with blueprintId: " + blueprintId);
    }

    private LotroCardBlueprint tryLoadingFromPackage(String packageName, String setNumber, String cardNumber) throws IllegalAccessException, InstantiationException {
        try {
            Class clazz = Class.forName("com.gempukku.lotro.cards.set" + setNumber + packageName + ".Card" + setNumber + "_" + normalizeId(cardNumber));
            return (LotroCardBlueprint) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            // Ignore
            return null;
        }
    }

    private String normalizeId(String blueprintPart) {
        int id = Integer.parseInt(blueprintPart);
        if (id < 10)
            return "00" + id;
        else if (id < 100)
            return "0" + id;
        else
            return String.valueOf(id);
    }
}
