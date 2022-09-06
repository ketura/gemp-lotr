package com.gempukku.lotro.packs;

import java.util.List;
import java.util.Map;

public class PackDefinition {

    public enum PackType {
        SELECTION, PACK, RANDOM, TENGWAR, RANDOM_FOIL, BOOSTER
    }

    public String Name;
    public PackType Type;
    public List<String> Items;
    public Map<String, String> Data;
}
