package com.gempukku.lotro.game;

import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.LotroCardBlueprintBuilder;
import com.gempukku.lotro.common.AppConfig;
import com.gempukku.lotro.game.packs.SetDefinition;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.hjson.JsonValue;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class LotroCardBlueprintLibrary {
    private static final Logger logger = Logger.getLogger(LotroCardBlueprintLibrary.class);

    private final String[] _packageNames =
            new String[]{
                    "", ".dwarven", ".dunland", ".elven", ".fallenRealms", ".gandalf", ".gollum", ".gondor", ".isengard", ".men", ".orc",
                    ".raider", ".rohan", ".moria", ".wraith", ".sauron", ".shire", ".site", ".uruk_hai",

                    //Additional Hobbit Draft packages
                    ".esgaroth", ".gundabad", ".smaug", ".spider", ".troll"
            };
    private final Map<String, LotroCardBlueprint> _blueprintMap = new HashMap<>();

    private final Map<String, String> _blueprintMapping = new HashMap<>();
    private final Map<String, Set<String>> _fullBlueprintMapping = new HashMap<>();

    private final LotroCardBlueprintBuilder cardBlueprintBuilder = new LotroCardBlueprintBuilder();

    public CountDownLatch collectionReadyLatch = new CountDownLatch(1);

    public LotroCardBlueprintLibrary() {
        try {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(AppConfig.getResourceStream("blueprintMapping.txt"), StandardCharsets.UTF_8))) {
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    if (!line.startsWith("#")) {
                        String[] split = line.split(",");
                        _blueprintMapping.put(split[0], split[1]);
                        addAlternatives(split[0], split[1]);
                    }
                }
            }
        } catch (IOException exp) {
            throw new RuntimeException("Problem loading blueprintMapping.txt", exp);
        }
    }

    private void initCardSets(CardSets cardSets) {
        for (SetDefinition setDefinition : cardSets.getSetDefinitions().values()) {
            if (setDefinition.hasFlag("needsLoading")) {
                logger.debug("Loading set " + setDefinition.getSetId());
                final Set<String> allCards = setDefinition.getAllCards();
                for (String blueprintId : allCards) {
                    if (getBaseBlueprintId(blueprintId).equals(blueprintId)) {
                        if (!_blueprintMap.containsKey(blueprintId)) {
                            try {
                                // Ensure it's loaded
                                LotroCardBlueprint blueprint = getBlueprint(blueprintId);
                                _blueprintMap.put(blueprintId, blueprint);
                            } catch (CardNotFoundException exp) {
                                throw new RuntimeException("Unable to start the server, due to invalid (missing) card definition - " + blueprintId);
                            }
                        }
                    }
                }
            }
        }
    }

    public void init(File cardPath, CardSets cardSets) {
        loadCards(cardPath, true);
        initCardSets(cardSets);
        collectionReadyLatch.countDown();
    }

    public void reloadCards(File cardPath) {
        loadCards(cardPath, false);
    }

    private void loadCards(File path, boolean initial) {
        if (path.isFile()) {
            loadCardsFromFile(path, initial);
        }
        else if (path.isDirectory()) {
            for (File file : path.listFiles()) {
                loadCards(file, initial);
            }
        }
    }

    private void loadCardsFromFile(File file, boolean validateNew) {
        String ext = FilenameUtils.getExtension(file.getName());
        if (!ext.equalsIgnoreCase("json") && !ext.equalsIgnoreCase("hjson"))
            return;

        JSONParser parser = new JSONParser();
        try (Reader reader = new InputStreamReader(new FileInputStream(file), "UTF-8")) {
            //This will read both json and hjson, producing standard json
            String json = JsonValue.readHjson(reader).toString();
            final JSONObject cardsFile = (JSONObject) parser.parse(json);
            final Set<Map.Entry<String, JSONObject>> cardsInFile = cardsFile.entrySet();
            for (Map.Entry<String, JSONObject> cardEntry : cardsInFile) {
                String blueprint = cardEntry.getKey();
                if (validateNew)
                    if (_blueprintMap.containsKey(blueprint))
                        logger.error(blueprint + " - Replacing existing card definition!");
                final JSONObject cardDefinition = cardEntry.getValue();
                try {
                    final LotroCardBlueprint lotroCardBlueprint = cardBlueprintBuilder.buildFromJson(cardDefinition);
                    _blueprintMap.put(blueprint, lotroCardBlueprint);
                } catch (InvalidCardDefinitionException exp) {
                    logger.error("Unable to load card " + blueprint, exp);
                }
            }
        } catch (FileNotFoundException exp) {
            logger.error("Failed to find file " + file.getAbsolutePath(), exp);
        } catch (IOException exp) {
            logger.error("Error while loading file " + file.getAbsolutePath(), exp);
        } catch (ParseException exp) {
            logger.error("Failed to parse file " + file.getAbsolutePath(), exp);
        }
        catch (Exception exp) {
            logger.error("Unexpected error while parsing file " + file.getAbsolutePath(), exp);
        }
        logger.debug("Loaded card file " + file.getName());
    }

    public String getBaseBlueprintId(String blueprintId) {
        blueprintId = stripBlueprintModifiers(blueprintId);
        String base = _blueprintMapping.get(blueprintId);
        if (base != null)
            return base;
        return blueprintId;
    }

    private void addAlternatives(String newBlueprint, String existingBlueprint) {
        Set<String> existingAlternates = _fullBlueprintMapping.get(existingBlueprint);
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
        Set<String> list = _fullBlueprintMapping.get(from);
        if (list == null) {
            list = new HashSet<>();
            _fullBlueprintMapping.put(from, list);
        }
        list.add(to);
    }

    public Map<String, LotroCardBlueprint> getBaseCards() {
        try {
            collectionReadyLatch.await();
            return Collections.unmodifiableMap(_blueprintMap);
        } catch (InterruptedException exp) {
            throw new RuntimeException("Interrupted', exp");
        }
    }

    public Set<String> getAllAlternates(String blueprintId) {
        return _fullBlueprintMapping.get(blueprintId);
    }

    public boolean hasAlternateInSet(String blueprintId, int setNo) {
        Set<String> alternatives = _fullBlueprintMapping.get(blueprintId);
        if (alternatives != null)
            for (String alternative : alternatives)
                if (alternative.startsWith(setNo + "_"))
                    return true;

        return false;
    }

    public LotroCardBlueprint getLotroCardBlueprint(String blueprintId) throws CardNotFoundException {
        blueprintId = stripBlueprintModifiers(blueprintId);

        if (_blueprintMap.containsKey(blueprintId))
            return _blueprintMap.get(blueprintId);

        return getBlueprint(blueprintId);
    }

    public String stripBlueprintModifiers(String blueprintId) {
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

    private LotroCardBlueprint getBlueprint(String blueprintId) throws CardNotFoundException {
        if (_blueprintMapping.containsKey(blueprintId))
            return getLotroCardBlueprint(_blueprintMapping.get(blueprintId));

        String[] blueprintParts = blueprintId.split("_");

        String setNumber = blueprintParts[0];
        String cardNumber = blueprintParts[1];

        for (String packageName : _packageNames) {
            LotroCardBlueprint blueprint;
            try {
                blueprint = tryLoadingFromPackage(packageName, setNumber, cardNumber);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException e) {
                throw new CardNotFoundException(blueprintId);
            }
            if (blueprint != null)
                return blueprint;
        }

        throw new CardNotFoundException(blueprintId);
    }

    private LotroCardBlueprint tryLoadingFromPackage(String packageName, String setNumber, String cardNumber) throws IllegalAccessException, InstantiationException, NoSuchMethodException {
        try {
            Class clazz = Class.forName("com.gempukku.lotro.cards.set" + setNumber + packageName + ".Card" + setNumber + "_" + normalizeId(cardNumber));
            return (LotroCardBlueprint) clazz.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InvocationTargetException e) {
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
