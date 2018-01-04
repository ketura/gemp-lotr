package com.gempukku.lotro.draft2;

import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.formats.LotroFormatLibrary;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class SoloDraftDefinitions {
    private Map<String, SoloDraft> draftTypes = new HashMap<String, SoloDraft>();

    public SoloDraftDefinitions() {
        try {
            final InputStreamReader reader = new InputStreamReader(LotroFormatLibrary.class.getResourceAsStream("/lotrDrafts.json"), "UTF-8");
            try {
                JSONParser parser = new JSONParser();
                JSONArray object = (JSONArray) parser.parse(reader);
                for (Object draftDefObj : object) {
                    String type = (String)((JSONObject) draftDefObj).get("type");
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
            final InputStreamReader reader = new InputStreamReader(LotroFormatLibrary.class.getResourceAsStream(file), "UTF-8");
            try {
                JSONParser parser = new JSONParser();
                JSONObject object = (JSONObject) parser.parse(reader);
                String format = (String) object.get("format");
                return null;
            } catch (ParseException exp) {
                throw new RuntimeException("Problem loading solo draft "+file, exp);
            }
        } catch (IOException exp) {
            throw new RuntimeException("Problem loading solo draft "+file, exp);
        }
    }

    public SoloDraft getSoloDraft(String draftType) {
        return draftTypes.get(draftType);
    }
}
