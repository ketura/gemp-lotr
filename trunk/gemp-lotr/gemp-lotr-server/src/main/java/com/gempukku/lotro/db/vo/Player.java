package com.gempukku.lotro.db.vo;

public class Player {
    private int _id;
    private String _name;

    public Player(int id, String name) {
        _id = id;
        _name = name;
    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }
}
