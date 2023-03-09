package com.gempukku.lotro.db;

import com.gempukku.lotro.common.DBDefs;
import com.gempukku.lotro.game.Player;
import org.sql2o.Sql2o;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DbPlayerDAO implements PlayerDAO {
    private final String validLoginChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
    private final String _selectPlayer = """
        SELECT 
            id, 
            name, 
            password, 
            type, 
            last_login_reward, 
            banned_until, 
            create_ip, 
            last_ip 
        FROM player
        """;


    private final DbAccess _dbAccess;

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

        try (Connection conn = _dbAccess.getDataSource().getConnection()) {
            String sql = _selectPlayer + " where password=?";
            if (player.getCreateIp() != null)
                sql += " or create_ip=? or last_ip=?";
            if (player.getLastIp() != null)
                sql += " or create_ip=? or last_ip=?";

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, player.getPassword());
                int nextParamIndex = 2;
                if (player.getCreateIp() != null) {
                    statement.setString(nextParamIndex, player.getCreateIp());
                    statement.setString(nextParamIndex + 1, player.getCreateIp());
                    nextParamIndex += 2;
                }
                if (player.getLastIp() != null) {
                    statement.setString(nextParamIndex, player.getLastIp());
                    statement.setString(nextParamIndex + 1, player.getLastIp());
                    nextParamIndex += 2;
                }
                try (ResultSet rs = statement.executeQuery()) {
                    List<Player> players = new LinkedList<>();
                    while (rs.next())
                        players.add(getPlayerFromResultSet(rs));
                    return players;
                }
            }
        }
    }

    @Override
    public Set<String> getBannedUsernames() {
        try {
            try (Connection connection = _dbAccess.getDataSource().getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("SELECT name FROM player WHERE type = '' ORDER BY ID DESC LIMIT 50")) {

                    try (ResultSet resultSet = statement.executeQuery()) {
                        TreeSet<String> users = new TreeSet<>();
                        while (resultSet.next()) {
                            users.add(resultSet.getString(1));
                        }
                        return users;
                    }
                }
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to get banned users", exp);
        }
    }

    @Override
    public boolean resetUserPassword(String login) throws SQLException {
        try {
            Sql2o db = new Sql2o(_dbAccess.getDataSource());

            try (org.sql2o.Connection conn = db.beginTransaction()) {
                String sql = """
                                UPDATE player
                                SET password = ''
                                WHERE name = :login
                            """;
                conn.createQuery(sql)
                        .addParameter("login", login)
                        .executeUpdate();

                conn.commit();
                return true;
            }
        } catch (Exception ex) {
            throw new RuntimeException("Unable to reset password", ex);
        }
    }

    @Override
    public boolean banPlayerPermanently(String login) throws SQLException {
        try (Connection conn = _dbAccess.getDataSource().getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement("update player set type='', banned_until=null where name=?")) {
                statement.setString(1, login);
                return statement.executeUpdate() == 1;
            }
        }
    }

    @Override
    public boolean banPlayerTemporarily(String login, long dateTo) throws SQLException {
        try (Connection conn = _dbAccess.getDataSource().getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement("update player set banned_until=?, type='un' where name=?")) {
                statement.setLong(1, dateTo);
                statement.setString(2, login);
                return statement.executeUpdate() == 1;
            }
        }
    }

    @Override
    public boolean unBanPlayer(String login) throws SQLException {
        try (Connection conn = _dbAccess.getDataSource().getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement("update player set type='un', banned_until=null where name=?")) {
                statement.setString(1, login);
                return statement.executeUpdate() == 1;
            }
        }
    }

    @Override
    public boolean addPlayerFlag(String login, String flag) throws SQLException {
        try (Connection conn = _dbAccess.getDataSource().getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement("UPDATE player SET type = CONCAT(type, ?) WHERE name=? AND type NOT LIKE CONCAT('%', ?, '%');")) {
                statement.setString(1, flag);
                statement.setString(2, login);
                statement.setString(3, flag);
                return statement.executeUpdate() == 1;
            }
        }
    }

    @Override
    public boolean removePlayerFlag(String login, String flag) throws SQLException {
        try (Connection conn = _dbAccess.getDataSource().getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement("UPDATE player SET type = REPLACE(type, ?, '') WHERE name=? AND type LIKE CONCAT('%', ?, '%');")) {
                statement.setString(1, flag);
                statement.setString(2, login);
                statement.setString(3, flag);
                return statement.executeUpdate() == 1;
            }
        }
    }

    @Override
    public Player loginUser(String login, String password) throws SQLException {

        try {
            Sql2o db = new Sql2o(_dbAccess.getDataSource());

            try (org.sql2o.Connection conn = db.open()) {
                String sql = _selectPlayer +
                        """
                            WHERE name = :login
                                AND (password = :password OR password = '')
                        """;
                List<DBDefs.Player> result = conn.createQuery(sql)
                        .addParameter("login", login)
                        .addParameter("password", encodePassword(password))
                        .executeAndFetch(DBDefs.Player.class);

                var def = result.stream().findFirst().orElse(null);
                if(def == null)
                    return null;

                return new Player(def);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Unable to retrieve login entries", ex);
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
        try (Connection conn = _dbAccess.getDataSource().getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement("update player set last_login_reward =? where id=?")) {
                statement.setInt(1, currentReward);
                statement.setInt(2, player.getId());
                statement.execute();
                player.setLastLoginReward(currentReward);
            }
        }
    }

    @Override
    public synchronized boolean updateLastReward(Player player, int previousReward, int currentReward) throws SQLException {
        try (Connection conn = _dbAccess.getDataSource().getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement("update player set last_login_reward =? where id=? and last_login_reward=?")) {
                statement.setInt(1, currentReward);
                statement.setInt(2, player.getId());
                statement.setInt(3, previousReward);
                if (statement.executeUpdate() == 1) {
                    player.setLastLoginReward(currentReward);
                    return true;
                }
                return false;
            }
        }
    }

    @Override
    public synchronized boolean registerUser(String login, String password, String remoteAddr) throws SQLException, LoginInvalidException {
        if (!validLoginName(login))
            return false;

        if(loginExists(login)) {
            if(!needsPasswordReset(login))
                return false;

            //Login exists but has a blank/null password, meaning this user is actually performing a password reset
            try {
                Sql2o db = new Sql2o(_dbAccess.getDataSource());

                try (org.sql2o.Connection conn = db.beginTransaction()) {
                    String sql = """
                                UPDATE player
                                SET password = :password
                                WHERE name = :login
                            """;
                    conn.createQuery(sql)
                            .addParameter("login", login)
                            .addParameter("password", encodePassword(password))
                            .executeUpdate();

                    conn.commit();
                    return true;
                }
            } catch (Exception ex) {
                throw new RuntimeException("Unable to update password", ex);
            }
        }


        try {
            Sql2o db = new Sql2o(_dbAccess.getDataSource());

            try (org.sql2o.Connection conn = db.beginTransaction()) {
                String sql = """
                                INSERT INTO player (name, password, type, create_ip)
                                VALUES (:login, :password, :type, :create_ip)
                            """;
                conn.createQuery(sql)
                        .addParameter("login", login)
                        .addParameter("password", encodePassword(password))
                        .addParameter("type", Player.Type.USER.toString())
                        .addParameter("create_ip", remoteAddr)
                        .executeUpdate();

                conn.commit();
                return true;
            }
        } catch (Exception ex) {
            throw new RuntimeException("Unable to insert new user", ex);
        }
    }

    private boolean validLoginName(String login) throws LoginInvalidException {
        if (login.length() < 2 || login.length() > 30)
            throw new LoginInvalidException();
        for (int i = 0; i < login.length(); i++) {
            char c = login.charAt(i);
            if (!validLoginChars.contains("" + c))
                throw new LoginInvalidException();
        }

        String lowerCase = login.toLowerCase();
        if (lowerCase.startsWith("admin") || lowerCase.startsWith("guest") || lowerCase.startsWith("system") || lowerCase.startsWith("bye"))
            return false;

        return true;
    }

    private boolean loginExists(String login) throws SQLException {

        try {
            Sql2o db = new Sql2o(_dbAccess.getDataSource());

            try (org.sql2o.Connection conn = db.open()) {
                String sql = _selectPlayer +
                        """
                            WHERE LOWER(name) = :login
                        """;
                List<DBDefs.Player> result = conn.createQuery(sql)
                        .addParameter("login", login.toLowerCase())
                        .executeAndFetch(DBDefs.Player.class);

                var def = result.stream().findFirst().orElse(null);
                return def != null;
            }
        } catch (Exception ex) {
            throw new RuntimeException("Unable to retrieve password reset entries", ex);
        }
    }

    private boolean needsPasswordReset(String login) throws SQLException {
        try {
            Sql2o db = new Sql2o(_dbAccess.getDataSource());

            try (org.sql2o.Connection conn = db.open()) {
                String sql = _selectPlayer +
                        """
                            WHERE LOWER(name) = :login
                                AND (password = '' OR password IS NULL)
                        """;
                List<DBDefs.Player> result = conn.createQuery(sql)
                        .addParameter("login", login.toLowerCase())
                        .executeAndFetch(DBDefs.Player.class);

                var def = result.stream().findFirst().orElse(null);
                return def != null;
            }
        } catch (Exception ex) {
            throw new RuntimeException("Unable to retrieve password reset entries", ex);
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
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private Player getPlayerFromDBById(int id) throws SQLException {
        try (Connection conn = _dbAccess.getDataSource().getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(_selectPlayer + " where id=?")) {
                statement.setInt(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return getPlayerFromResultSet(rs);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    private Player getPlayerFromDBByName(String playerName) throws SQLException {
        try (Connection conn = _dbAccess.getDataSource().getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(_selectPlayer + " where name=?")) {
                statement.setString(1, playerName);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return getPlayerFromResultSet(rs);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    @Override
    public void updateLastLoginIp(String login, String remoteAddr) throws SQLException {
        try (Connection conn = _dbAccess.getDataSource().getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement("update player set last_ip=? where name=?")) {
                statement.setString(1, remoteAddr);
                statement.setString(2, login);
                statement.execute();
            }
        }
    }

    @Override
    public List<DBDefs.Player> getAllPlayers() {

        try {

            Sql2o db = new Sql2o(_dbAccess.getDataSource());

            try (org.sql2o.Connection conn = db.open()) {
                String sql = "SELECT id, name FROM player";
                List<DBDefs.Player> result = conn.createQuery(sql)
                        .executeAndFetch(DBDefs.Player.class);

                return result;
            }
        } catch (Exception ex) {
            throw new RuntimeException("Unable to retrieve players", ex);
        }
    }
}
