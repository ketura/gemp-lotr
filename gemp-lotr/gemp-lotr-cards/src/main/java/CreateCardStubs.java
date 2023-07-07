import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.packs.SetDefinition;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CreateCardStubs {
    private static final String[] _packageNames =
            new String[]{
                    "", ".dwarven", ".dunland", ".elven", ".fallenRealms", ".gandalf", ".gollum", ".gondor", ".isengard", ".men", ".orc",
                    ".raider", ".rohan", ".moria", ".wraith", ".sauron", ".shire", ".site", ".uruk_hai",

                    //Additional Hobbit Draft packages
                    ".esgaroth", ".gundabad", ".smaug", ".spider", ".troll"
            };

    public static void main(String[] args) {
        final String property = System.getProperty("user.dir");
        String projectRoot = new File(property).getAbsolutePath();

        File path = new File(projectRoot + "/gemp-lotr-async/src/main/web/cards-stub");

//        int[] sets = {2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 15, 17, 18, 31};
        int[] sets = {2, 3, 4, 5};
        for (int set : sets) {
            produceForSet(path, set);
        }
    }

    private static void produceForSet(File root, int set) {
        File setPath = new File(root, "set" + set);

        LotroCardBlueprintLibrary library = new LotroCardBlueprintLibrary();
        Map<String, Map<String, LotroCardBlueprint>> cardsByFileName = new HashMap<>();

        SetDefinition setDefinition = library.getSetDefinitions().get("" + set);
        if (setDefinition.hasFlag("needsLoading")) {
            final Set<String> allCards = setDefinition.getAllCards();
            for (String blueprintId : allCards) {
                blueprintId = stripBlueprintModifiers(blueprintId);
                try {
                    // Ensure it's loaded
                    LotroCardBlueprint cardBlueprint = getBlueprint(blueprintId);
                    String key = getKey(cardBlueprint);
                    Map<String, LotroCardBlueprint> list = cardsByFileName.computeIfAbsent(key, k -> new LinkedHashMap<>());
                    list.put(blueprintId, cardBlueprint);
                } catch (CardNotFoundException exp) {
                    // Ignore card that is not defined
                }
            }
        }

        for (Map.Entry<String, Map<String, LotroCardBlueprint>> cardsOfType : cardsByFileName.entrySet()) {
            String type = cardsOfType.getKey();
            File typePath = new File(setPath, "set" + set + "-" + type + ".json");
            if (!typePath.exists()) {
                JSONObject jsonObject = new JSONObject();

                Map<String, LotroCardBlueprint> cards = cardsOfType.getValue();
                for (Map.Entry<String, LotroCardBlueprint> cardEntry : cards.entrySet()) {
                    String blueprintId = cardEntry.getKey();
                    LotroCardBlueprint card = cardEntry.getValue();

                    JSONObject cardJson = new JSONObject();
                    cardJson.put("title", (card.isUnique() ? "*" : "") + card.getTitle());
                    if (card.getSubtitle() != null)
                        cardJson.put("subtitle", card.getSubtitle());
                    if (card.getSide() != null)
                        cardJson.put("side", card.getSide().name().toLowerCase());
                    if (card.getCulture() != null)
                        cardJson.put("culture", card.getCulture().getHumanReadable());
                    cardJson.put("cost", card.getTwilightCost());
                    cardJson.put("type", card.getCardType().name().toLowerCase());
                    if (card.getRace() != null)
                        cardJson.put("race", card.getRace().getHumanReadable());
                    if (card.getPossessionClasses() != null) {
                        JSONArray array = new JSONArray();
                        for (PossessionClass possessionClass : card.getPossessionClasses()) {
                            array.add(possessionClass.getHumanReadable());
                        }

                        if (array.size() == 1)
                            cardJson.put("possession", array.get(0));
                        else
                            cardJson.put("possession", array);
                    }
                    if (card.getSignet() != null)
                        cardJson.put("signet", card.getSignet().name().toLowerCase());
                    if (card.getStrength() != 0)
                        cardJson.put("strength", card.getStrength());
                    if (card.getVitality() != 0)
                        cardJson.put("vitality", card.getVitality());
                    if (card.getResistance() != 0)
                        cardJson.put("resistance", card.getResistance());
                    if (card.getCardType() == CardType.ALLY) {
                        if (card.getAllyHomeSiteBlock() != null) {
                            String home = card.getAllyHomeSiteBlock().getHumanReadable();
                            for (int allyHomeSiteNumber : card.getAllyHomeSiteNumbers()) {
                                home += "," + allyHomeSiteNumber;
                            }

                            cardJson.put("allyHome", home);
                        }
                    }
                    if (card.getCardType() == CardType.SITE)
                        cardJson.put("block", card.getSiteBlock().getHumanReadable());
                    if (card.getSiteNumber() != 0)
                        cardJson.put("site", card.getSiteNumber());
                    if (card.getCardType() == CardType.SITE)
                        cardJson.put("direction", card.getSiteDirection().name().toLowerCase());

                    // Keywords handling
                    JSONArray keywords = new JSONArray();
                    for (Keyword keyword : Keyword.values()) {
                        if (card.hasKeyword(keyword)) {
                            int count = card.getKeywordCount(keyword);
                            if (count > 1)
                                keywords.add(keyword.getHumanReadable() + "+" + count);
                            else
                                keywords.add(keyword.getHumanReadable());
                        }
                    }

                    if (keywords.size() == 1)
                        cardJson.put("keyword", keywords.get(0));
                    if (keywords.size() > 1)
                        cardJson.put("keyword", keywords);

                    jsonObject.put(blueprintId, cardJson);
                }

                typePath.getParentFile().mkdirs();
                //Write JSON file
                try (FileWriter file = new FileWriter(typePath)) {

                    file.write(jsonObject.toJSONString());
                    file.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getKey(LotroCardBlueprint cardBlueprint) {
        if (cardBlueprint.getCulture() != null)
            return cardBlueprint.getCulture().getHumanReadable().toLowerCase();
        if (cardBlueprint.getCardType() == CardType.SITE)
            return "site";
        return "ring";
    }

    public static String stripBlueprintModifiers(String blueprintId) {
        if (blueprintId.endsWith("*"))
            blueprintId = blueprintId.substring(0, blueprintId.length() - 1);
        if (blueprintId.endsWith("T"))
            blueprintId = blueprintId.substring(0, blueprintId.length() - 1);
        return blueprintId;
    }

    private static LotroCardBlueprint getBlueprint(String blueprintId) throws CardNotFoundException {
        String[] blueprintParts = blueprintId.split("_");

        String setNumber = blueprintParts[0];
        String cardNumber = blueprintParts[1];

        for (String packageName : _packageNames) {
            LotroCardBlueprint blueprint = null;
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

    private static LotroCardBlueprint tryLoadingFromPackage(String packageName, String setNumber, String cardNumber) throws IllegalAccessException, InstantiationException, NoSuchMethodException {
        try {
            Class clazz = Class.forName("com.gempukku.lotro.cards.set" + setNumber + packageName + ".Card" + setNumber + "_" + normalizeId(cardNumber));
            return (LotroCardBlueprint) clazz.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InvocationTargetException e) {
            // Ignore
            return null;
        }
    }

    private static String normalizeId(String blueprintPart) {
        int id = Integer.parseInt(blueprintPart);
        if (id < 10)
            return "00" + id;
        else if (id < 100)
            return "0" + id;
        else
            return String.valueOf(id);
    }
}
