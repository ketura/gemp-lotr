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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (_id != player._id) return false;
        if (_name != null ? !_name.equals(player._name) : player._name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + (_name != null ? _name.hashCode() : 0);
        return result;
    }
}
