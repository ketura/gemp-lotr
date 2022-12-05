package com.gempukku.lotro.common;

import java.util.*;

public class JSONDefs {
    public static class Pack {
        public enum PackType {
            SELECTION, PACK, RANDOM, TENGWAR, RANDOM_FOIL, BOOSTER
        }

        public String Name;
        public PackType Type;
        public List<String> Items;
        public Map<String, String> Data;
    }

    public static class SealedTemplate {
        public String Name;
        public String ID;
        public String Format;
        public List<List<String>> SeriesProduct;
    }

    public static class ItemStub {
        public String code;
        public String name;
        public ItemStub(String c, String n) {
            code = c;
            name = n;
        }
    }

    public static class Format {
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
        public ArrayList<Integer> errataSets = new ArrayList<>();
        public Map<String, String> errata = new HashMap<>();
        public boolean hall = true;

    }

    public static class FullFormatReadout {
        public Map<String, Format> Formats;
        public Map<String, SealedTemplate> SealedTemplates;
        public Map<String, ItemStub> DraftTemplates;
    }
}
