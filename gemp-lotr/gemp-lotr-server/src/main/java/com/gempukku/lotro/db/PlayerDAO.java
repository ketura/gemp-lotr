package com.gempukku.lotro.db;

import com.gempukku.lotro.common.DBDefs;
import com.gempukku.lotro.game.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface PlayerDAO {
    public User getPlayer(int id);

    public User getPlayer(String playerName);

    public boolean resetUserPassword(String login) throws SQLException;

    public boolean banPlayerPermanently(String login) throws SQLException;

    public boolean banPlayerTemporarily(String login, long dateTo) throws SQLException;

    public boolean unBanPlayer(String login) throws SQLException;

    public boolean addPlayerFlag(String login, User.Type flag) throws SQLException;
    public boolean removePlayerFlag(String login, User.Type flag) throws SQLException;

    public List<User> findSimilarAccounts(String login) throws SQLException;
    public Set<String> getBannedUsernames() throws SQLException;

    public User loginUser(String login, String password) throws SQLException;

    public void setLastReward(User player, int currentReward) throws SQLException;

    public boolean updateLastReward(User player, int previousReward, int currentReward) throws SQLException;

    public boolean registerUser(String login, String password, String remoteAddr) throws SQLException, LoginInvalidException;

    public void updateLastLoginIp(String login, String remoteAddr) throws SQLException;

    List<DBDefs.Player> getAllPlayers();
}
