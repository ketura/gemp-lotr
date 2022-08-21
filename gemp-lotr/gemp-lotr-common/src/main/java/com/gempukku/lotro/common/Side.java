package com.gempukku.lotro.common;

public enum Side implements Filterable {
    FREE_PEOPLE, SHADOW;

    public static Side Parse(String value) {
        value = value
                .toLowerCase()
                .replace(" ", "")
                .replace("_", "")
                .replace("-", "");

        if(value.contains("shadow"))
            return SHADOW;
        if(value.contains("freeps") || value.contains("free") || value.contains("people"))
            return FREE_PEOPLE;

        return null;
    }
}
