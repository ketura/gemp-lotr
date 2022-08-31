package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.common.AppConfig;
import com.gempukku.lotro.game.AdventureLibrary;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.LotroFormat;
import com.alibaba.fastjson.JSON;
import org.sql2o.tools.IOUtils;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LotroFormatLibrary {
    private final Map<String, LotroFormat> _allFormats = new HashMap<>();
    private final Map<String, LotroFormat> _hallFormats = new LinkedHashMap<>();


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
        public ArrayList<String> banned = new ArrayList<>();
        public ArrayList<String> restricted = new ArrayList<>();
        public ArrayList<String> valid = new ArrayList<>();
        public ArrayList<String> limit2 = new ArrayList<>();
        public ArrayList<String> limit3 = new ArrayList<>();
        public ArrayList<String> restrictedName = new ArrayList<>();
        public Map<String, String> errata = new HashMap<>();
        public boolean hall = true;

        public FormatDefinition() {
            set = new ArrayList<>();
            banned = new ArrayList<>();
            restricted = new ArrayList<>();
            valid = new ArrayList<>();
            limit2 = new ArrayList<>();
            limit3 = new ArrayList<>();
            restrictedName = new ArrayList<>();
            errata = new HashMap<>();
        }
    }

    public LotroFormatLibrary(AdventureLibrary adventureLibrary, LotroCardBlueprintLibrary bpLibrary) {
        try {
            try (InputStreamReader reader = new InputStreamReader(AppConfig.getResourceStream("lotrFormats.json"), StandardCharsets.UTF_8)) {
                String json = IOUtils.toString(reader);

                FormatDefinition[] formatDefs = JSON.parseObject(json, FormatDefinition[].class);

                for (FormatDefinition def : formatDefs) {
                    if (def == null)
                        continue;

                    DefaultLotroFormat format = new DefaultLotroFormat(adventureLibrary, bpLibrary, def);

                    _allFormats.put(format.getCode(), format);
                    if (format.hallVisible()) {
                        _hallFormats.put(format.getCode(), format);
                    }
                }

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
        return _allFormats.values().stream()
                .filter(lotroFormat -> lotroFormat.getName().equals(formatName))
                .findFirst()
                .get();
    }
}
