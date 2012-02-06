package com.gempukku.lotro.db.vo;

import java.util.Collections;
import java.util.Map;

public class LeagueSerie {
    private String _leagueType;
    private String _type;
    private String _format;
    private Map<String, Integer> _serieCollection;
    private int _status;
    private int _maxMatches;
    private int _start;
    private int _end;

    public LeagueSerie(String leagueType, String type, String format, Map<String, Integer> serieCollection, int status, int maxMatches, int start, int end) {
        _leagueType = leagueType;
        _type = type;
        _format = format;
        _serieCollection = serieCollection;
        _status = status;
        _maxMatches = maxMatches;
        _start = start;
        _end = end;
    }

    public int getMaxMatches() {
        return _maxMatches;
    }

    public String getLeagueType() {
        return _leagueType;
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

    public boolean wasCollectionGiven() {
        return _status == 1;
    }

    public Map<String, Integer> getSerieCollection() {
        return Collections.unmodifiableMap(_serieCollection);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeagueSerie that = (LeagueSerie) o;

        if (_leagueType != null ? !_leagueType.equals(that._leagueType) : that._leagueType != null) return false;
        if (_type != null ? !_type.equals(that._type) : that._type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _leagueType != null ? _leagueType.hashCode() : 0;
        result = 31 * result + (_type != null ? _type.hashCode() : 0);
        result = 31 * result + (_format != null ? _format.hashCode() : 0);
        result = 31 * result + _maxMatches;
        result = 31 * result + _start;
        result = 31 * result + _end;
        return result;
    }
}
