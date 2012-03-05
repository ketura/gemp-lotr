package com.gempukku.lotro.db.vo;

public final class CollectionType {
    private final String _code;
    private final String _fullName;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CollectionType that = (CollectionType) o;

        if (_code != null ? !_code.equals(that._code) : that._code != null) return false;
        if (_fullName != null ? !_fullName.equals(that._fullName) : that._fullName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _code != null ? _code.hashCode() : 0;
        result = 31 * result + (_fullName != null ? _fullName.hashCode() : 0);
        return result;
    }
}
