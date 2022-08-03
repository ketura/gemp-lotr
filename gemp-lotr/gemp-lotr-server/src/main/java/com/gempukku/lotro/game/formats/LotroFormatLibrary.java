package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.game.AdventureLibrary;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.LotroFormat;
import com.alibaba.fastjson.JSON;
import org.sql2o.tools.IOUtils;

import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.*;

public class LotroFormatLibrary {
    private Map<String, LotroFormat> _allFormats = new HashMap<String, LotroFormat>();
    private Map<String, LotroFormat> _hallFormats = new LinkedHashMap<String, LotroFormat>();


    public static class FormatDefinition {
        public String adventure;
        public String code;
        public String name;
        public int order = 1000;
        public String surveyUrl;
        public String sites;
        public boolean cancelRingBearerSkirmish = false;
        public boolean ruleOfFour = true;
        public boolean winAtEndOfRegroup = false;
        public boolean winOnControlling5Sites = false;
        public boolean playtest = false;
        public boolean validateShadowFPCount = true;
        public int minimumDeckSize = 60;
        public int maximumSameName = 4;
        public boolean mulliganRule = true;
        public ArrayList<Integer> set;
        public ArrayList<String> banned = new ArrayList<String>();
        public ArrayList<String> restricted = new ArrayList<String>();
        public ArrayList<String> valid = new ArrayList<String>();
        public ArrayList<String> limit2 = new ArrayList<String>();
        public ArrayList<String> limit3 = new ArrayList<String>();
        public ArrayList<String> restrictedName = new ArrayList<String>();
        public Map<String, String> errata = new HashMap<>();
        public boolean hall = true;

        public FormatDefinition() {
            set = new ArrayList<>();
            banned = new ArrayList<String>();
            restricted = new ArrayList<String>();
            valid = new ArrayList<String>();
            limit2 = new ArrayList<String>();
            limit3 = new ArrayList<String>();
            restrictedName = new ArrayList<String>();
            errata = new HashMap<>();
        }
    }

    public LotroFormatLibrary(AdventureLibrary adventureLibrary, LotroCardBlueprintLibrary bpLibrary) {
        try {
            final InputStreamReader reader = new InputStreamReader(LotroFormatLibrary.class.getResourceAsStream("/lotrFormats.json"), "UTF-8");
            try {
                String json = IOUtils.toString(reader);

                FormatDefinition[] formatDefs = JSON.parseObject(json, FormatDefinition[].class);

                for(FormatDefinition def : formatDefs) {
                    if(def == null)
                        continue;

                    DefaultLotroFormat format = new DefaultLotroFormat(adventureLibrary, bpLibrary, def);

                    _allFormats.put(format.getCode(), format);
                    if (format.hallVisible()) {
                        _hallFormats.put(format.getCode(), format);
                    }
                }

            } finally {
                reader.close();
            }
        } catch (Exception exp) {
            throw new RuntimeException("Problem loading LotR formats", exp);
        }
    }

    public Map<String, LotroFormat> getHallFormats() {
        return Collections.unmodifiableMap(_hallFormats);
    }

    public LotroFormat getFormat(String formatCode) {
        return _allFormats.get(formatCode);
    }

    public LotroFormat getFormatByName(String formatName) {
        return _allFormats.entrySet().stream()
                .filter(x -> x.getValue().getName().equals(formatName))
                .map(Map.Entry::getValue)
                .findFirst()
                .get();
    }
}
