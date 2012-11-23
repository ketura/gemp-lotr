package com.gempukku.lotro.db;

import com.gempukku.lotro.game.Player;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DbPlayerDAO implements PlayerDAO {
    private final String validLoginChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";

    private DbAccess _dbAccess;
    private Map<String, Player> _playersByName = new ConcurrentHashMap<String, Player>();
    private Map<Integer, Player> _playersById = new ConcurrentHashMap<Integer, Player>();

    public DbPlayerDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    public synchronized void clearCache() {
        _playersByName.clear();
        _playersById.clear();
    }

    public synchronized Player getPlayer(int id) {
        if (_playersById.containsKey(id))
            return _playersById.get(id);

        try {
            final Player player = getPlayerFromDBById(id);
            if (player != null)
                _playersById.put(id, player);
            return player;
        } catch (SQLException exp) {
            throw new RuntimeException("Error while retrieving player", exp);
        }
    }

    public synchronized Player getPlayer(String playerName) {
        if (_playersByName.containsKey(playerName))
            return _playersByName.get(playerName);
        try {
            Player player = getPlayerFromDBByName(playerName);
            if (player != null)
                _playersByName.put(playerName, player);
            return player;
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to get player from DB", exp);
        }
    }

    public synchronized Player loginUser(String login, String password) throws SQLException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("select id, name, type, last_login_reward from player where name=? and password=?");
            try {
                statement.setString(1, login);
                statement.setString(2, encodePassword(password));
                ResultSet rs = statement.executeQuery();
                try {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        String name = rs.getString(2);
                        String type = rs.getString(3);
                        Integer lastLoginReward = rs.getInt(4);
                        if (rs.wasNull())
                            lastLoginReward = null;

                        return new Player(id, name, type, lastLoginReward);
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

    public synchronized void setLastReward(Player player, int currentReward) throws SQLException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("update player set last_login_reward =? where id=?");
            try {
                statement.setInt(1, currentReward);
                statement.setInt(2, player.getId());
                statement.execute();
                player.setLastLoginReward(currentReward);
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }

    public synchronized boolean updateLastReward(Player player, int previousReward, int currentReward) throws SQLException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("update player set last_login_reward =? where id=? and last_login_reward=?");
            try {
                statement.setInt(1, currentReward);
                statement.setInt(2, player.getId());
                statement.setInt(3, previousReward);
                if (statement.executeUpdate() == 1) {
                    player.setLastLoginReward(currentReward);
                    return true;
                }
                return false;
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }

    public synchronized boolean registerUser(String login, String password, String remoteAddr) throws SQLException {
        boolean result = validateLogin(login);
        if (!result)
            return false;

        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("insert into player (name, password, type, create_ip) values (?, ?, ?, ?)");
            try {
                statement.setString(1, login);
                statement.setString(2, encodePassword(password));
                statement.setString(3, "u");
                statement.setString(4, remoteAddr);
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
        if (lowerCase.startsWith("admin") || lowerCase.startsWith("guest") || lowerCase.startsWith("system") || lowerCase.startsWith("bye"))
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

    private Player getPlayerFromDBById(int id) throws SQLException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("select id, name, type, last_login_reward from player where id=?");
            try {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                try {
                    if (rs.next()) {
                        String name = rs.getString(2);
                        String type = rs.getString(3);
                        Integer lastLoginReward = rs.getInt(4);
                        if (rs.wasNull())
                            lastLoginReward = null;

                        return new Player(id, name, type, lastLoginReward);
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

    private Player getPlayerFromDBByName(String playerName) throws SQLException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("select id, name, type, last_login_reward from player where name=?");
            try {
                statement.setString(1, playerName);
                ResultSet rs = statement.executeQuery();
                try {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        String name = rs.getString(2);
                        String type = rs.getString(3);
                        Integer lastLoginReward = rs.getInt(4);
                        if (rs.wasNull())
                            lastLoginReward = null;

                        return new Player(id, name, type, lastLoginReward);
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

    public void updateLastLoginIp(String login, String remoteAddr) throws SQLException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("update player set last_ip=? where name=?");
            try {
                statement.setString(1, remoteAddr);
                statement.setString(2, login);
                statement.execute();
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }
}
