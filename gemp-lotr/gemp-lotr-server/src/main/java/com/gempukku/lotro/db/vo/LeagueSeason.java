package com.gempukku.lotro.db.vo;

public class LeagueSeason {
    private String _type;
    private int _maxMatches;

    public LeagueSeason(String type, int maxMatches) {
        _type = type;
        _maxMatches = maxMatches;
    }

    public int getMaxMatches() {
        return _maxMatches;
    }

    public String getType() {
        return _type;
    }
}
