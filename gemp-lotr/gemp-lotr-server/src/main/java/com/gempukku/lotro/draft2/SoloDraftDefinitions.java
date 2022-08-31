package com.gempukku.lotro.draft2;

import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.common.AppConfig;
import com.gempukku.lotro.draft2.builder.CardCollectionProducer;
import com.gempukku.lotro.draft2.builder.DraftChoiceBuilder;
import com.gempukku.lotro.draft2.builder.DraftPoolBuilder;
import com.gempukku.lotro.draft2.builder.DraftPoolProducer;
import com.gempukku.lotro.draft2.builder.StartingPoolBuilder;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.formats.LotroFormatLibrary;
import com.gempukku.lotro.game.packs.SetDefinition;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SoloDraftDefinitions {
    private static final Logger _logger = Logger.getLogger(SoloDraftDefinitions.class);
    private final Map<String, SoloDraft> draftTypes = new HashMap<>();
    private final StartingPoolBuilder startingPoolBuilder = new StartingPoolBuilder();
    private final DraftPoolBuilder draftPoolBuilder = new DraftPoolBuilder();
    private final DraftChoiceBuilder draftChoiceBuilder;

    public SoloDraftDefinitions(CollectionsManager collectionsManager, LotroCardBlueprintLibrary cardLibrary,
                                LotroFormatLibrary formatLibrary, Map<String, SetDefinition> rarities) {
        draftChoiceBuilder = new DraftChoiceBuilder(collectionsManager, cardLibrary, formatLibrary, rarities);
        try {
            final InputStreamReader reader = new InputStreamReader(AppConfig.getResourceStream("lotrDrafts.json"), StandardCharsets.UTF_8);
            try {
                JSONParser parser = new JSONParser();
                JSONArray object = (JSONArray) parser.parse(reader);
                for (Object draftDefObj : object) {
                    String type = (String) ((JSONObject) draftDefObj).get("type");
                    String location = (String) ((JSONObject) draftDefObj).get("location");
                    draftTypes.put(type, loadDraft(location));
                }
            } catch (ParseException exp) {
                throw new RuntimeException("Problem loading solo drafts", exp);
            }
        } catch (IOException exp) {
            throw new RuntimeException("Problem loading solo drafts", exp);
        }
    }

    private SoloDraft loadDraft(String file) {
        try {
            final InputStreamReader reader = new InputStreamReader(LotroFormatLibrary.class.getResourceAsStream(file), StandardCharsets.UTF_8);
            try {
                JSONParser parser = new JSONParser();
                JSONObject object = (JSONObject) parser.parse(reader);
                String format = (String) object.get("format");

                CardCollectionProducer cardCollectionProducer = null;
                JSONObject startingPool = (JSONObject) object.get("startingPool");
                if (startingPool != null)
                    cardCollectionProducer = startingPoolBuilder.buildCardCollectionProducer(startingPool);

                DraftPoolProducer draftPoolProducer = null;
                JSONArray draftPoolComponents = (JSONArray) object.get("draftPool");
                if (draftPoolComponents != null)
                    draftPoolProducer = draftPoolBuilder.buildDraftPoolProducer(draftPoolComponents);

                List<DraftChoiceDefinition> draftChoiceDefinitions = new ArrayList<>();
                JSONArray choices = (JSONArray) object.get("choices");
                for (JSONObject choice : (Iterable<JSONObject>) choices) {
                    DraftChoiceDefinition draftChoiceDefinition = draftChoiceBuilder.buildDraftChoiceDefinition(choice);
                    int repeatCount = ((Number) choice.get("repeat")).intValue();
                    for (int i = 0; i < repeatCount; i++)
                        draftChoiceDefinitions.add(draftChoiceDefinition);
                }

                _logger.debug("Loaded draft definition: "+file);
                return new DefaultSoloDraft(format, cardCollectionProducer, draftChoiceDefinitions, draftPoolProducer);
            } catch (ParseException exp) {
                throw new RuntimeException("Problem loading solo draft " + file, exp);
            }
        } catch (IOException exp) {
            throw new RuntimeException("Problem loading solo draft " + file, exp);
        }
    }

    public SoloDraft getSoloDraft(String draftType) {
        return draftTypes.get(draftType);
    }
}
