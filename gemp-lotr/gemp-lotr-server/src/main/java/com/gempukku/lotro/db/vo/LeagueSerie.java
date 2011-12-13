package com.gempukku.lotro.db.vo;

public class LeagueSerie {
    private String _type;
    private int _maxMatches;
    private int _start;
    private int _end;

    public LeagueSerie(String type, int maxMatches, int start, int end) {
        _type = type;
        _maxMatches = maxMatches;
        _start = start;
        _end = end;
    }

    public int getMaxMatches() {
        return _maxMatches;
    }

    public String getType() {
        return _type;
    }

    public int getEnd() {
        return _end;
    }

    public int getStart() {
        return _start;
    }
}
