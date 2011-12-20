package com.gempukku.lotro.db.vo;

public class LeagueSerie {
    private String _type;
    private String _format;
    private int _maxMatches;
    private int _start;
    private int _end;

    public LeagueSerie(String type, String format, int maxMatches, int start, int end) {
        _type = type;
        _format = format;
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

    public String getFormat() {
        return _format;
    }

    public int getEnd() {
        return _end;
    }

    public int getStart() {
        return _start;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeagueSerie that = (LeagueSerie) o;

        if (_type != null ? !_type.equals(that._type) : that._type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return _type != null ? _type.hashCode() : 0;
    }
}
