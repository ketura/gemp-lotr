package com.gempukku.lotro.packs;

import com.gempukku.lotro.common.AppConfig;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.util.JsonUtils;
import org.apache.commons.io.FilenameUtils;
import org.hjson.JsonValue;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class ProductLibrary {
    public static class OuterPackDef {

    }
    private final Map<String, PackBox> _products = new HashMap<>();
    private final LotroCardBlueprintLibrary _cardLibrary;
    private final File _packDirectory;

    public ProductLibrary(LotroCardBlueprintLibrary cardLibrary) {
        this(cardLibrary, AppConfig.getProductPath());
    }
    public ProductLibrary(LotroCardBlueprintLibrary cardLibrary, File packDefinitionDirectory) {
        _cardLibrary = cardLibrary;
        _packDirectory = packDefinitionDirectory;
        init();
    }

    private void init() {
        loadPacks(_packDirectory);
    }

    private void loadPacks(File path) {
        if (path.isFile()) {
            loadPackFromFile(path);
        }
        else if (path.isDirectory()) {
            for (File file : path.listFiles()) {
                loadPacks(file);
            }
        }
    }

    private void loadPackFromFile(File file) {
        String ext = FilenameUtils.getExtension(file.getName());
        if (!ext.equalsIgnoreCase("json") && !ext.equalsIgnoreCase("hjson"))
            return;
        JSONParser parser = new JSONParser();
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            var defs = JsonUtils.ConvertArray(reader, PackDefinition.class);

            if(defs == null)
            {
                var def= JsonUtils.Convert(reader, PackDefinition.class);
                if(def != null)
                {
                    defs = new ArrayList<>();
                    defs.add(def);
                }
                else {
                    System.out.println(file.toString() + " is not a PackDefinition nor an array of PackDefinitions.  Could not load from file.");
                    return;
                }
            }

            for (var def : defs) {
                if(def == null)
                    continue;

                PackBox result = null;
                String rarity;
                String[] sets;
                switch (def.Type)
                {
                    case RANDOM:
                        if(def.Items == null || def.Items.isEmpty())
                            continue;
                        result = UnweightedRandomPack.LoadFromArray(def.Items);
                        break;
                    case RANDOM_FOIL:
                        if(def.Data == null || !def.Data.containsKey("rarity") || !def.Data.containsKey("sets")) {
                            System.out.println(def.Name + " RANDOM_FOIL pack type must contain a definition for 'rarity' and 'sets' within data.");
                            continue;
                        }
                        rarity = def.Data.get("rarity").toUpperCase();
                        sets = def.Data.get("sets").split("\\s*,\\s*");
                        result = new RandomFoilPack(rarity, sets, _cardLibrary);
                        break;
                    case TENGWAR:
                        if(def.Data == null || !def.Data.containsKey("sets")) {
                            System.out.println(def.Name + " TENGWAR pack type must contain a definition for 'sets' within data.");
                            continue;
                        }
                        sets = def.Data.get("sets").split("\\s*,\\s*");
                        result = new TengwarPackBox(sets, _cardLibrary);
                        break;
                    case BOOSTER:
                        if(def.Data == null || !def.Data.containsKey("set")) {
                            System.out.println(def.Name + " BOOSTER pack type must contain a definition for 'set' within data.");
                            continue;
                        }
                        if(def.Data.get("set").contains(",")) {
                            System.out.println(def.Name + " BOOSTER pack type must define exactly one set.");
                            continue;
                        }
                        String set = def.Data.get("set").trim();
                        if(set.equals("9")) {
                            result = new ReflectionsPackBox(_cardLibrary);
                        }
                        else {
                            result = new RarityPackBox(_cardLibrary.getSetDefinitions().get(set));
                        }
                        break;
                    case PACK:
                    case SELECTION:
                        if(def.Items == null || def.Items.isEmpty())
                            continue;
                        result = FixedPackBox.LoadFromArray(def.Items);
                        break;
                }
                if(result == null)
                {
                    System.out.println("Unrecognized pack type: " + def.Type);
                    continue;
                }

//                if(def.InstantOpen) {
//                    var items = result.openPack().stream().map(CardCollection.Item::toString).toList();
//                    result = FixedPackBox.LoadFromArray(items);
//                }
                if(_products.containsKey(def.Name)) {
                    System.out.println("Overwriting existing pack '" + def.Name + "'!");
                }
                _products.put(def.Name, result);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, PackBox> GetAllProducts() {
        return Collections.unmodifiableMap(_products);
    }



    public PackBox GetProduct(String name) {
        return _products.get(name);
    }
}

