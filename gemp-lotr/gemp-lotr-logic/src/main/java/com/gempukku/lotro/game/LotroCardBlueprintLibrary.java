package com.gempukku.lotro.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LotroCardBlueprintLibrary {
    private String[] _packageNames =
            new String[]{
                    "", ".dwarven", ".dunland", ".elven", ".gandalf", ".gollum", ".gondor", ".isengard", ".raider", ".rohan", ".moria", ".wraith", ".sauron", ".shire", ".site"
            };
    private Map<String, LotroCardBlueprint> _blueprintMap = new HashMap<String, LotroCardBlueprint>();

    private Map<String, String> _blueprintMapping = new HashMap<String, String>();
    private Map<String, List<String>> _fullBlueprintMapping = new HashMap<String, List<String>>();

    public LotroCardBlueprintLibrary() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(LotroCardBlueprintLibrary.class.getResourceAsStream("/blueprintMapping.txt"), "UTF-8"));
            try {
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    String[] split = line.split(",");
                    _blueprintMapping.put(split[0], split[1]);
                    addAlternatives(split[0], split[1]);
                }
            } finally {
                bufferedReader.close();
            }
        } catch (IOException exp) {
            throw new RuntimeException("Problem loading blueprint mapping", exp);
        }
    }

    public String getBaseBlueprintId(String blueprintId) {
        blueprintId = stripBlueprintModifiers(blueprintId);
        String base = _blueprintMapping.get(blueprintId);
        if (base != null)
            return base;
        return blueprintId;
    }

    private void addAlternatives(String newBlueprint, String existingBlueprint) {
        List<String> existingAlternates = _fullBlueprintMapping.get(existingBlueprint);
        if (existingAlternates != null) {
            for (String existingAlternate : existingAlternates) {
                addAlternative(newBlueprint, existingAlternate);
                addAlternative(existingAlternate, newBlueprint);
            }
        }
        addAlternative(newBlueprint, existingBlueprint);
        addAlternative(existingBlueprint, newBlueprint);
    }

    private void addAlternative(String from, String to) {
        List<String> list = _fullBlueprintMapping.get(from);
        if (list == null) {
            list = new LinkedList<String>();
            _fullBlueprintMapping.put(from, list);
        }
        list.add(to);
    }

    public boolean hasAlternateInSet(String blueprintId, int setNo) {
        List<String> alternatives = _fullBlueprintMapping.get(blueprintId);
        if (alternatives != null)
            for (String alternative : alternatives)
                if (alternative.startsWith(setNo + "_"))
                    return true;

        return false;
    }

    public LotroCardBlueprint getLotroCardBlueprint(String blueprintId) {
        blueprintId = stripBlueprintModifiers(blueprintId);

        if (_blueprintMap.containsKey(blueprintId))
            return _blueprintMap.get(blueprintId);

        LotroCardBlueprint blueprint = getBlueprint(blueprintId);
        _blueprintMap.put(blueprintId, blueprint);
        return blueprint;
    }

    private String stripBlueprintModifiers(String blueprintId) {
        if (blueprintId.endsWith("*"))
            blueprintId = blueprintId.substring(0, blueprintId.length() - 1);
        if (blueprintId.endsWith("T"))
            blueprintId = blueprintId.substring(0, blueprintId.length() - 1);
        return blueprintId;
    }

    //    public void initializeLibrary(String setNo, int maxCardIndex) {
//        for (int i = 1; i <= maxCardIndex; i++) {
//            try {
//                getLotroCardBlueprint(setNo + "_" + i);
//            } catch (IllegalArgumentException exp) {
//                // Ignore
//            }
//        }
//    }
//
//    public Collection<LotroCardBlueprint> getAllLoadedBlueprints() {
//        return _blueprintMap.values();
//    }

    //

    private LotroCardBlueprint getBlueprint(String blueprintId) {
        if (_blueprintMapping.containsKey(blueprintId))
            return getBlueprint(_blueprintMapping.get(blueprintId));

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
