package com.gempukku.lotro.game;

import com.gempukku.lotro.game.packs.DefaultSetDefinition;
import com.gempukku.lotro.game.packs.SetDefinition;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class CardSets {
    private Map<String, SetDefinition> _allSets = new LinkedHashMap<String, SetDefinition>();

    public CardSets() {
        try {
            final InputStreamReader reader = new InputStreamReader(CardSets.class.getResourceAsStream("/setConfig.json"), "UTF-8");
            try {
                JSONParser parser = new JSONParser();
                JSONArray object = (JSONArray) parser.parse(reader);
                for (Object setDefinitionObj : object) {
                    JSONObject setDefinition = (JSONObject) setDefinitionObj;

                    String setId = (String) setDefinition.get("setId");
                    String setName = (String) setDefinition.get("setName");
                    String rarityFile = (String) setDefinition.get("rarityFile");

                    Set<String> flags = new HashSet<String>();
                    determineOriginalSetFlag(setDefinition, flags);
                    determineMerchantableFlag(setDefinition, flags);
                    determineNeedsLoadingFlag(setDefinition, flags);

                    DefaultSetDefinition rarity = new DefaultSetDefinition(setId, setName, flags);

                    readSetRarityFile(rarity, setId, rarityFile);
                    
                    _allSets.put(setId, rarity);
                }
            } finally {
                IOUtils.closeQuietly(reader);
            }
        } catch (ParseException e) {
            throw new RuntimeException("Unable to parse setConfig.json file");
        } catch (IOException exp) {
            throw new RuntimeException("Unable to read card rarities");
        }
    }

    private void determineNeedsLoadingFlag(JSONObject setDefinition, Set<String> flags) {
        Boolean needsLoading = (Boolean) setDefinition.get("needsLoading");
        if (needsLoading == null)
            needsLoading = true;
        if (needsLoading)
            flags.add("needsLoading");
    }

    private void determineMerchantableFlag(JSONObject setDefinition, Set<String> flags) {
        Boolean merchantable = (Boolean) setDefinition.get("merchantable");
        if (merchantable == null)
            merchantable = true;
        if (merchantable)
            flags.add("merchantable");
    }

    private void determineOriginalSetFlag(JSONObject setDefinition, Set<String> flags) {
        Boolean originalSet = (Boolean) setDefinition.get("originalSet");
        if (originalSet == null)
            originalSet = true;
        if (originalSet)
            flags.add("originalSet");
    }

    private void readSetRarityFile(DefaultSetDefinition rarity, String setNo, String rarityFile) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(CardSets.class.getResourceAsStream(rarityFile), "UTF-8"));
        try {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.endsWith("T")) {
                    if (!line.substring(0, setNo.length()).equals(setNo))
                        throw new IllegalStateException("Seems the rarity is for some other set");
                    String blueprintId = setNo + "_" + line.substring(setNo.length() + 1);
                    rarity.addTengwarCard(blueprintId);
                } else {
                    if (!line.substring(0, setNo.length()).equals(setNo))
                        throw new IllegalStateException("Seems the rarity is for some other set");
                    String cardRarity = line.substring(setNo.length(), setNo.length() + 1);
                    String blueprintId = setNo + "_" + line.substring(setNo.length() + 1);
                    rarity.addCard(blueprintId, cardRarity);
                }
            }
        } finally {
            IOUtils.closeQuietly(bufferedReader);
        }
    }

    public Map<String, SetDefinition> getSetDefinitions() {
        return Collections.unmodifiableMap(_allSets);
    }

    public static void main(String[] args) {
        CardSets cardSets = new CardSets();
        System.out.println(cardSets.getSetDefinitions().size());
    }
}
