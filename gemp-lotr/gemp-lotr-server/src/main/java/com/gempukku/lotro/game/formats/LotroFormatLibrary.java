package com.gempukku.lotro.game.formats;

import com.alibaba.fastjson.JSON;
import com.gempukku.lotro.common.AppConfig;
import com.gempukku.lotro.common.JSONDefs;
import com.gempukku.lotro.game.AdventureLibrary;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.LotroFormat;
import com.gempukku.lotro.league.SealedLeagueDefinition;
import com.gempukku.util.JsonUtils;
import org.json.simple.parser.JSONParser;
import org.sql2o.tools.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class LotroFormatLibrary {
    private final Map<String, LotroFormat> _allFormats = new HashMap<>();
    private final Map<String, LotroFormat> _hallFormats = new LinkedHashMap<>();

    private final Map<String, SealedLeagueDefinition> _sealedTemplates = new LinkedHashMap<>();

    private final AdventureLibrary _adventureLibrary;
    private final LotroCardBlueprintLibrary _cardLibrary;
    private final File _formatPath;
    private final File _sealedPath;


    private final Semaphore collectionReady = new Semaphore(1);

    public LotroFormatLibrary(AdventureLibrary adventureLibrary, LotroCardBlueprintLibrary bpLibrary) {
        this(adventureLibrary, bpLibrary, AppConfig.getFormatDefinitionsPath(), AppConfig.getSealedPath());
    }

    public LotroFormatLibrary(AdventureLibrary adventureLibrary, LotroCardBlueprintLibrary bpLibrary, File formatPath, File sealedPath) {
        _adventureLibrary = adventureLibrary;
        _cardLibrary = bpLibrary;
        _formatPath = formatPath;
        _sealedPath = sealedPath;

        ReloadFormats();
        ReloadSealedTemplates();
    }

    public void ReloadSealedTemplates() {
        try {
            collectionReady.acquire();
            _sealedTemplates.clear();
            loadSealedTemplates(_sealedPath);
            collectionReady.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadSealedTemplates(File path) {
        if (path.isFile()) {
            loadTemplateFromFile(path);
        }
        else if (path.isDirectory()) {
            for (File file : path.listFiles()) {
                loadSealedTemplates(file);
            }
        }
    }

    private void loadTemplateFromFile(File file) {
        if (!JsonUtils.IsValidHjsonFile(file))
            return;
        JSONParser parser = new JSONParser();
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            var defs = JsonUtils.ConvertArray(reader, JSONDefs.SealedTemplate.class);

            if(defs == null)
            {
                var def= JsonUtils.Convert(reader, JSONDefs.SealedTemplate.class);
                if(def != null)
                {
                    defs = Collections.singletonList(def);
                }
                else {
                    System.out.println(file.toString() + " is not a SealedTemplate nor an array of SealedTemplate.  Could not load from file.");
                    return;
                }
            }

            for (var def : defs) {
                if(def == null)
                    continue;
                var sealed = new SealedLeagueDefinition(def.Name, _allFormats.get(def.Format), def.SeriesProduct);

                if(_sealedTemplates.containsKey(def.Name)) {
                    System.out.println("Overwriting existing sealed definition '" + def.Name + "'!");
                }
                _sealedTemplates.put(def.Name, sealed);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void ReloadFormats() {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(_formatPath), StandardCharsets.UTF_8)) {
            collectionReady.acquire();
            String json = IOUtils.toString(reader);

            JSONDefs.Format[] formatDefs = JSON.parseObject(json, JSONDefs.Format[].class);

            _allFormats.clear();
            _hallFormats.clear();

            for (JSONDefs.Format def : formatDefs) {
                if (def == null)
                    continue;

                DefaultLotroFormat format = new DefaultLotroFormat(_adventureLibrary, _cardLibrary, def);

                _allFormats.put(format.getCode(), format);
                if (format.hallVisible()) {
                    _hallFormats.put(format.getCode(), format);
                }
            }
            collectionReady.release();
        }
        catch (Exception exp) {
            throw new RuntimeException("Problem loading LotR formats", exp);
        }
    }

    public Map<String, LotroFormat> getHallFormats() {
        try {
            collectionReady.acquire();
            var data = Collections.unmodifiableMap(_hallFormats);
            collectionReady.release();
            return data;
        }
        catch (InterruptedException exp) {
            throw new RuntimeException("FormatLibrary.getHallFormats() interrupted: ", exp);
        }
    }

    public LotroFormat getFormat(String formatCode) {
        try {
            collectionReady.acquire();
            var data = _allFormats.get(formatCode);
            collectionReady.release();
            return data;
        }
        catch (InterruptedException exp) {
            throw new RuntimeException("FormatLibrary.getFormat() interrupted: ", exp);
        }
    }

    public LotroFormat getFormatByName(String formatName) {
        try {
            collectionReady.acquire();
            var data = _allFormats.values().stream()
                    .filter(lotroFormat -> lotroFormat.getName().equals(formatName))
                    .findFirst()
                    .orElse(null);
            collectionReady.release();
            return data;
        }
        catch (InterruptedException exp) {
            throw new RuntimeException("FormatLibrary.getFormatByName() interrupted: ", exp);
        }
    }

    private Map<String, String> legacyCodeMapping = new HashMap<>() {{
        put("fotr_block", "Fellowship Block Sealed");
        put("ttt_block", "Towers Block Sealed");
        put("movie", "King Block Sealed");
        put("war_block", "War of the Ring Block Sealed");
        put("hunters_block", "Hunters Block Sealed");
        put("movie_special", "Movie Special Sealed");
        put("ts_special", "Towers Standard Special Sealed");
    }};

    public SealedLeagueDefinition GetSealedTemplate(String leagueName) {
        try {
            collectionReady.acquire();
            var data = _sealedTemplates.get(leagueName);
            if(data == null) {
                data = _sealedTemplates.get(legacyCodeMapping.get(leagueName));
            }
            collectionReady.release();
            return data;
        }
        catch (InterruptedException exp) {
            throw new RuntimeException("FormatLibrary.GetSealedTemplate() interrupted: ", exp);
        }
    }

    public SealedLeagueDefinition GetSealedTemplateByFormatCode(String formatCode) {
        try {
            collectionReady.acquire();
            var data = _sealedTemplates.values().stream()
                    .filter(x -> x.GetFormat().getCode().equals(formatCode))
                    .findFirst()
                    .orElse(null);
            collectionReady.release();
            return data;
        }
        catch (InterruptedException exp) {
            throw new RuntimeException("FormatLibrary.GetSealedTemplateByFormatCode() interrupted: ", exp);
        }
    }
}
