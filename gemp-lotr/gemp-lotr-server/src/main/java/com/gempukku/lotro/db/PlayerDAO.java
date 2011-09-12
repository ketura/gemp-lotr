package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDAO {
    private DbAccess _dbAccess;
    private Map<String, Player> _players = new ConcurrentHashMap<String, Player>();

    public PlayerDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    public Player getPlayer(String playerName) {
        if (_players.containsKey(playerName))
            return _players.get(playerName);
        else {
            try {
                Player player = getPlayerFromDB(playerName);
                if (player != null) {
                    _players.put(playerName, player);
                    return player;
                } else
                    return null;
            } catch (SQLException exp) {
                throw new RuntimeException("Unable to get player from DB", exp);
            }
        }
    }

    private Player getPlayerFromDB(String playerName) throws SQLException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("select id, name from player where name=?");
            try {
                statement.setString(1, playerName);
                ResultSet rs = statement.executeQuery();
                try {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        String name = rs.getString(2);

                        return new Player(id, name);
                    } else {
                        return null;
                    }
                } finally {
                    rs.close();
                }
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }
}
