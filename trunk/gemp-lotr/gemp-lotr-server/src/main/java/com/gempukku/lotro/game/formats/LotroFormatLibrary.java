package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.LotroFormat;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LotroFormatLibrary {
    private Map<String, LotroFormat> _allFormats = new HashMap<String, LotroFormat>();
    private Map<String, LotroFormat> _hallFormats = new LinkedHashMap<String, LotroFormat>();

    public LotroFormatLibrary(LotroCardBlueprintLibrary library) {
        try {
            final InputStreamReader reader = new InputStreamReader(LotroFormatLibrary.class.getResourceAsStream("/lotrFormats.json"), "UTF-8");
            try {
                JSONParser parser = new JSONParser();
                JSONArray object = (JSONArray) parser.parse(reader);
                for (Object formatDefObj : object) {
                    JSONObject formatDef = (JSONObject) formatDefObj;
                    String formatCode = (String) formatDef.get("code");
                    String name = (String) formatDef.get("name");
                    Block block = Block.valueOf((String) formatDef.get("sites"));
                    Boolean cancelRingBearerSkirmish = (Boolean) formatDef.get("cancelRingBearerSkirmish");
                    if (cancelRingBearerSkirmish == null)
                        cancelRingBearerSkirmish = false;

                    final DefaultLotroFormat format = new DefaultLotroFormat(library, name, block, true, 60, 4, true, cancelRingBearerSkirmish);

                    JSONArray sets = (JSONArray) formatDef.get("set");
                    for (Object set : sets)
                        format.addValidSet(((Number) set).intValue());

                    JSONArray bannedCards = (JSONArray) formatDef.get("banned");
                    if (bannedCards != null)
                        for (Object bannedCard : bannedCards) {
                            format.addBannedCard((String) bannedCard);
                        }

                    JSONArray restrictedCards = (JSONArray) formatDef.get("restricted");
                    if (restrictedCards != null)
                        for (Object restricted : restrictedCards) {
                            format.addRestrictedCard((String) restricted);
                        }

                    JSONArray validCards = (JSONArray) formatDef.get("valid");
                    if (validCards != null)
                        for (Object valid : validCards) {
                            format.addValidCard((String) valid);
                        }

                    _allFormats.put(formatCode, format);

                    Boolean hallFormat = (Boolean) formatDef.get("hall");
                    if (hallFormat == null)
                        hallFormat = true;
                    if (hallFormat)
                        _hallFormats.put(formatCode, format);
                }
            } catch (ParseException exp) {
                throw new RuntimeException("Problem loading LotR formats", exp);
            } finally {
                reader.close();
            }
        } catch (IOException exp) {
            throw new RuntimeException("Problem loading LotR formats", exp);
        }
    }

    public Map<String, LotroFormat> getHallFormats() {
        return Collections.unmodifiableMap(_hallFormats);
    }

    public LotroFormat getFormat(String formatCode) {
        return _allFormats.get(formatCode);
    }
}
