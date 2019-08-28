package com.gempukku.lotro.game;

import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.LotroCardBlueprintBuilder;
import com.gempukku.lotro.game.packs.SetDefinition;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class LotroCardBlueprintLibrary {
    private static Logger logger = Logger.getLogger(LotroCardBlueprintLibrary.class);

    private String[] _packageNames =
            new String[]{
                    "", ".dwarven", ".dunland", ".elven", ".fallenRealms", ".gandalf", ".gollum", ".gondor", ".isengard", ".men", ".orc",
                    ".raider", ".rohan", ".moria", ".wraith", ".sauron", ".shire", ".site", ".uruk_hai",

                    //Additional Hobbit Draft packages
                    ".esgaroth", ".gundabad", ".smaug", ".spider", ".troll"
            };
    private Map<String, LotroCardBlueprint> _blueprintMap = new HashMap<String, LotroCardBlueprint>();

    private Map<String, String> _blueprintMapping = new HashMap<String, String>();
    private Map<String, Set<String>> _fullBlueprintMapping = new HashMap<String, Set<String>>();

    private LotroCardBlueprintBuilder cardBlueprintBuilder = new LotroCardBlueprintBuilder();

    public LotroCardBlueprintLibrary() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(LotroCardBlueprintLibrary.class.getResourceAsStream("/blueprintMapping.txt"), "UTF-8"));
            try {
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    if (!line.startsWith("#")) {
                        String[] split = line.split(",");
                        _blueprintMapping.put(split[0], split[1]);
                        addAlternatives(split[0], split[1]);
                    }
                }
            } finally {
                bufferedReader.close();
            }
        } catch (IOException exp) {
            throw new RuntimeException("Problem loading blueprint mapping", exp);
        }
    }

    public void initCardSets(CardSets cardSets) {
        for (SetDefinition setDefinition : cardSets.getSetDefinitions().values()) {
            if (setDefinition.hasFlag("playable")) {
                logger.debug("Loading set " + setDefinition.getSetId());
                final Set<String> allCards = setDefinition.getAllCards();
                for (String blueprintId : allCards) {
                    if (getBaseBlueprintId(blueprintId).equals(blueprintId)) {
                        try {
                            // Ensure it's loaded
                            LotroCardBlueprint cardBlueprint = getLotroCardBlueprint(blueprintId);
                        } catch (CardNotFoundException exp) {
                            throw new RuntimeException("Unable to start the server, due to invalid (missing) card definition - " + blueprintId);
                        }
                    }
                }
            }
        }
    }

    public void init(File cardPath) {
        loadCards(cardPath);
        setupWatcher(cardPath.toPath());
    }

    private void loadCards(File path) {
        for (File file : path.listFiles()) {
            loadCardsFromFile(file);
        }
    }

    private void loadCardsFromFile(File file) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(file)) {
            final JSONObject cardsFile = (JSONObject) parser.parse(reader);
            final Set<Map.Entry<String, JSONObject>> cardsInFile = cardsFile.entrySet();
            for (Map.Entry<String, JSONObject> cardEntry : cardsInFile) {
                String blueprint = cardEntry.getKey();
                final JSONObject cardDefinition = cardEntry.getValue();
                try {
                    final LotroCardBlueprint lotroCardBlueprint = cardBlueprintBuilder.buildFromJson(cardDefinition);
                    _blueprintMap.put(blueprint, lotroCardBlueprint);
                    logger.debug("Loaded card " + blueprint);
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
    }

    private void setupWatcher(Path path) {
        Thread thr = new Thread(
                () -> {
                    try {
                        WatchService watcher = FileSystems.getDefault().newWatchService();
                        WatchKey registrationKey = path.register(watcher,
                                ENTRY_CREATE,
                                ENTRY_MODIFY);

                        while (true) {
                            // wait for key to be signaled
                            WatchKey key;
                            try {
                                key = watcher.take();
                            } catch (InterruptedException x) {
                                return;
                            }

                            for (WatchEvent<?> event : key.pollEvents()) {
                                WatchEvent.Kind<?> kind = event.kind();

                                // This key is registered only
                                // for ENTRY_CREATE events,
                                // but an OVERFLOW event can
                                // occur regardless if events
                                // are lost or discarded.
                                if (kind == OVERFLOW) {
                                    continue;
                                }

                                // The filename is the
                                // context of the event.
                                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                                Path filename = ev.context();

                                // Resolve the filename against the directory.
                                // If the filename is "test" and the directory is "foo",
                                // the resolved name is "test/foo".
                                Path child = path.resolve(filename);

                                loadCardsFromFile(child.toFile());
                            }

                            // Reset the key -- this step is critical if you want to
                            // receive further watch events.  If the key is no longer valid,
                            // the directory is inaccessible so exit the loop.
                            boolean valid = key.reset();
                            if (!valid) {
                                break;
                            }
                        }
                    } catch (IOException exp) {
                        logger.error("Unable to setup folder watcher on " + path.toString(), exp);
                    }
                }
        );
        thr.start();
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
            list = new HashSet<String>();
            _fullBlueprintMapping.put(from, list);
        }
        list.add(to);
    }

    public Map<String, LotroCardBlueprint> getBaseCards() {
        return Collections.unmodifiableMap(_blueprintMap);
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

        LotroCardBlueprint blueprint = getBlueprint(blueprintId);
        _blueprintMap.put(blueprintId, blueprint);
        return blueprint;
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
            return getBlueprint(_blueprintMapping.get(blueprintId));

        String[] blueprintParts = blueprintId.split("_");

        String setNumber = blueprintParts[0];
        String cardNumber = blueprintParts[1];

        for (String packageName : _packageNames) {
            LotroCardBlueprint blueprint = null;
            try {
                blueprint = tryLoadingFromPackage(packageName, setNumber, cardNumber);
            } catch (IllegalAccessException e) {
                throw new CardNotFoundException();
            } catch (InstantiationException e) {
                throw new CardNotFoundException();
            }
            if (blueprint != null)
                return blueprint;
        }

        throw new CardNotFoundException();
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
