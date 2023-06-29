package com.gempukku.lotro.game;

public class BasicCardItem implements CardItem {
    private final String _blueprintId;

    public BasicCardItem(String blueprintId) {
        _blueprintId = blueprintId;
    }

    @Override
    public String getBlueprintId() {
        return _blueprintId;
    }

    @Override
    public boolean isRecursive() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicCardItem that = (BasicCardItem) o;

        if (_blueprintId != null ? !_blueprintId.equals(that._blueprintId) : that._blueprintId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return _blueprintId != null ? _blueprintId.hashCode() : 0;
    }
}

