package com.gempukku.lotro.db.vo;

public class CollectionType {
    private String _code;
    private String _fullName;

    public CollectionType(String code, String fullName) {
        _code = code;
        _fullName = fullName;
    }

    public String getCode() {
        return _code;
    }

    public String getFullName() {
        return _fullName;
    }
}
