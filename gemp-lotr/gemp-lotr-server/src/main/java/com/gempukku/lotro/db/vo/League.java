package com.gempukku.lotro.db.vo;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.MutableCardCollection;

public class League {
    private int _id;
    private String _type;
    private String _name;
    private MutableCardCollection _baseCollection;

    public League(int id, String type, String name, MutableCardCollection baseCollection) {
        _id = id;
        _type = type;
        _name = name;
        _baseCollection = baseCollection;
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

    public CardCollection getBaseCollection() {
        return _baseCollection;
    }
}
