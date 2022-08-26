package com.gempukku.lotro.game;

import java.util.Date;

public class Player {
    private final int _id;
    private final String _name;
    private final String _password;
    private final String _type;
    private Integer _lastLoginReward;
    private final Date _bannedUntil;
    private final String _createIp;
    private final String _lastIp;

    public Player(int id, String name, String password, String type, Integer lastLoginReward, Date bannedUntil, String createIp, String lastIp) {
        _id = id;
        _name = name;
        _password = password;
        _type = type;
        _lastLoginReward = lastLoginReward;
        _bannedUntil = bannedUntil;
        _createIp = createIp;
        _lastIp = lastIp;
    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public String getPassword() {
        return _password;
    }

    public String getType() {
        return _type;
    }

    public Integer getLastLoginReward() {
        return _lastLoginReward;
    }

    public void setLastLoginReward(int lastLoginReward) {
        _lastLoginReward = lastLoginReward;
    }

    public Date getBannedUntil() {
        return _bannedUntil;
    }

    public String getCreateIp() {
        return _createIp;
    }

    public String getLastIp() {
        return _lastIp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (_name != null ? !_name.equals(player._name) : player._name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return _name != null ? _name.hashCode() : 0;
    }

    public PlayerDefinition GetUserInfo() {
        return new PlayerDefinition(_name, _type);
    }

    public class PlayerDefinition {
        public String name;
        public String type;

        public PlayerDefinition(String name, String info) {
            this.name = name;
            type = info;
        }
    }
}
