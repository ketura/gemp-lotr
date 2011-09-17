package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.Player;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDAO {
    private final String validLoginChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";

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

    public Player loginUser(String login, String password) throws SQLException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("select id, name from player where name=? and password=?");
            try {
                statement.setString(1, login);
                statement.setString(2, encodePassword(password));
                ResultSet rs = statement.executeQuery();
                try {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        String name = rs.getString(2);

                        return new Player(id, name);
                    } else
                        return null;
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

    public synchronized boolean registerUser(String login, String password) throws SQLException {
        boolean result = validateLogin(login);
        if (!result)
            return false;

        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("insert into player (name, password, type) values (?, ?, ?)");
            try {
                statement.setString(1, login);
                statement.setString(2, encodePassword(password));
                statement.setString(3, "u");
                statement.execute();
                return true;
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }

    private boolean validateLogin(String login) throws SQLException {
        if (login.length() < 2 || login.length() > 10)
            return false;
        for (int i = 0; i < login.length(); i++) {
            char c = login.charAt(i);
            if (!validLoginChars.contains("" + c))
                return false;
        }

        String lowerCase = login.toLowerCase();
        if (lowerCase.startsWith("admin") || lowerCase.startsWith("guest"))
            return false;

        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("select id, name from player where LOWER(name)=?");
            try {
                statement.setString(1, lowerCase);
                ResultSet rs = statement.executeQuery();
                try {
                    if (rs.next()) {
                        return false;
                    } else
                        return true;
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

    private String encodePassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            return convertToHexString(digest.digest(password.getBytes("UTF-8")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String convertToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
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
