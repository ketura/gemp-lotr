package com.gempukku.lotro.db;

import com.gempukku.lotro.game.Player;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DbPlayerDAO implements PlayerDAO {
    private final String validLoginChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
    private final String _selectPlayer = "select id, name, password, type, last_login_reward, banned_until, create_ip, last_ip from player";

    private DbAccess _dbAccess;

    public DbPlayerDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    @Override
    public Player getPlayer(int id) {
        try {
            return getPlayerFromDBById(id);
        } catch (SQLException exp) {
            throw new RuntimeException("Error while retrieving player", exp);
        }
    }

    @Override
    public Player getPlayer(String playerName) {
        try {
            return getPlayerFromDBByName(playerName);
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to get player from DB", exp);
        }
    }

    @Override
    public List<Player> findSimilarAccounts(String login) throws SQLException {
        final Player player = getPlayerFromDBByName(login);
        if (player == null)
            return null;

        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            String sql = _selectPlayer + " where password=?";
            if (player.getCreateIp() != null)
                sql += " or create_ip=? or last_ip=?";
            if (player.getLastIp() != null)
                sql += " or create_ip=? or last_ip=?";

            PreparedStatement statement = conn.prepareStatement(sql);
            try {
                statement.setString(1, player.getPassword());
                int nextParamIndex = 2;
                if (player.getCreateIp() != null) {
                    statement.setString(nextParamIndex, player.getCreateIp());
                    statement.setString(nextParamIndex+1, player.getCreateIp());
                    nextParamIndex+=2;
                }
                if (player.getLastIp() != null) {
                    statement.setString(nextParamIndex, player.getLastIp());
                    statement.setString(nextParamIndex+1, player.getLastIp());
                    nextParamIndex+=2;
                }
                ResultSet rs = statement.executeQuery();
                try {
                    List<Player> players = new LinkedList<Player>();
                    while (rs.next())
                        players.add(getPlayerFromResultSet(rs));
                    return players;
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

    @Override
    public boolean banPlayerPermanently(String login) throws SQLException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("update player set type='', banned_until=null where name=?");
            try {
                statement.setString(1, login);
                return statement.executeUpdate() == 1;
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }

    @Override
    public boolean banPlayerTemporarily(String login, long dateTo) throws SQLException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("update player set banned_until=?, type='un' where name=?");
            try {
                statement.setLong(1, dateTo);
                statement.setString(2, login);
                return statement.executeUpdate() == 1;
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }

    @Override
    public boolean unBanPlayer(String login) throws SQLException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("update player set type='un', banned_until=null where name=?");
            try {
                statement.setString(1, login);
                return statement.executeUpdate() == 1;
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }

    @Override
    public Player loginUser(String login, String password) throws SQLException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement(_selectPlayer + " where name=? and password=?");
            try {
                statement.setString(1, login);
                statement.setString(2, encodePassword(password));
                ResultSet rs = statement.executeQuery();
                try {
                    if (rs.next()) {
                        return getPlayerFromResultSet(rs);
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

    private Player getPlayerFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        String name = rs.getString(2);
        String password = rs.getString(3);
        String type = rs.getString(4);
        Integer lastLoginReward = rs.getInt(5);
        if (rs.wasNull())
            lastLoginReward = null;
        Long bannedUntilLong = rs.getLong(6);
        if (rs.wasNull())
            bannedUntilLong = null;

        Date bannedUntil = null;
        if (bannedUntilLong != null)
            bannedUntil = new Date(bannedUntilLong);
        String createIp = rs.getString(7);
        String lastIp = rs.getString(8);

        return new Player(id, name, password, type, lastLoginReward, bannedUntil, createIp, lastIp);
    }

    @Override
    public void setLastReward(Player player, int currentReward) throws SQLException {
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

    @Override
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

    @Override
    public synchronized boolean registerUser(String login, String password, String remoteAddr) throws SQLException, LoginInvalidException {
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

    private boolean validateLogin(String login) throws SQLException, LoginInvalidException {
        if (login.length() < 2 || login.length() > 10)
            throw new LoginInvalidException();
        for (int i = 0; i < login.length(); i++) {
            char c = login.charAt(i);
            if (!validLoginChars.contains("" + c))
                throw new LoginInvalidException();
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
            PreparedStatement statement = conn.prepareStatement(_selectPlayer + " where id=?");
            try {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                try {
                    if (rs.next()) {
                        return getPlayerFromResultSet(rs);
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
            PreparedStatement statement = conn.prepareStatement(_selectPlayer + " where name=?");
            try {
                statement.setString(1, playerName);
                ResultSet rs = statement.executeQuery();
                try {
                    if (rs.next()) {
                        return getPlayerFromResultSet(rs);
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

    @Override
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
