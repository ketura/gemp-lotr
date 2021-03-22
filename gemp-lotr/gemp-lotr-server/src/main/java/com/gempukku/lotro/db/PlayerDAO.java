package com.gempukku.lotro.db;

import com.gempukku.lotro.game.Player;

import java.sql.SQLException;
import java.util.List;

public interface PlayerDAO {
    public Player getPlayer(int id);

    public Player getPlayer(String playerName);

    public boolean banPlayerPermanently(String login) throws SQLException;

    public boolean banPlayerTemporarily(String login, long dateTo) throws SQLException;

    public boolean unBanPlayer(String login) throws SQLException;

    public boolean addPlayerFlag(String login, String flag) throws SQLException;
    public boolean removePlayerFlag(String login, String flag) throws SQLException;

    public List<Player> findSimilarAccounts(String login) throws SQLException;

    public Player loginUser(String login, String password) throws SQLException;

    public void setLastReward(Player player, int currentReward) throws SQLException;

    public boolean updateLastReward(Player player, int previousReward, int currentReward) throws SQLException;

    public boolean registerUser(String login, String password, String remoteAddr) throws SQLException, LoginInvalidException;

    public void updateLastLoginIp(String login, String remoteAddr) throws SQLException;
}
