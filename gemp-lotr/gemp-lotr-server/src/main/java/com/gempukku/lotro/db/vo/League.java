package com.gempukku.lotro.db.vo;

import com.gempukku.lotro.league.LeagueData;

import java.lang.reflect.Constructor;

public class League {
    private int _cost;
    private String _name;
    private String _type;
    private String _clazz;
    private String _parameters;
    private int _status;

    public League(int cost, String name, String type, String clazz, String parameters, int status) {
        _cost = cost;
        _name = name;
        _type = type;
        _clazz = clazz;
        _parameters = parameters;
        _status = status;
    }

    public int getCost() {
        return _cost;
    }

    public String getName() {
        return _name;
    }

    public String getType() {
        return _type;
    }

    public LeagueData getLeagueData() {
        try {
            Class<?> aClass = Class.forName(_clazz);
            Constructor<?> constructor = aClass.getConstructor(String.class);
            return (LeagueData) constructor.newInstance(_parameters);
        } catch (Exception exp) {
            throw new RuntimeException("Unable to create LeagueData", exp);
        }
    }

    public int getStatus() {
        return _status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        League league = (League) o;

        if (_type != null ? !_type.equals(league._type) : league._type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return _type != null ? _type.hashCode() : 0;
    }
}
