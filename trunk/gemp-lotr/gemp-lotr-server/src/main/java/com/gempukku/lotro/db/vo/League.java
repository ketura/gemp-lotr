package com.gempukku.lotro.db.vo;

import com.gempukku.lotro.game.MutableCardCollection;

public class League {
    private int _id;
    private String _type;
    private String _name;
    private MutableCardCollection _baseCollection;
    private int _start;
    private int _end;

    public League(int id, String type, String name, MutableCardCollection baseCollection, int start, int end) {
        _id = id;
        _type = type;
        _name = name;
        _baseCollection = baseCollection;
        _start = start;
        _end = end;
    }

    public int getId() {
        return _id;
    }

    public String getType() {
        return _type;
    }

    public String getName() {
        return _name;
    }

    public MutableCardCollection getBaseCollection() {
        return _baseCollection;
    }

    public int getEnd() {
        return _end;
    }

    public int getStart() {
        return _start;
    }
}
