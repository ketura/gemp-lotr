package com.gempukku.lotro.db;

import com.gempukku.lotro.cache.Cached;
import com.gempukku.lotro.game.Player;
import org.apache.commons.collections.map.LRUMap;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

public class CachedPlayerDAO implements PlayerDAO, Cached {
    private PlayerDAO _delegate;
    private Map<Integer, Player> _playerById = Collections.synchronizedMap(new LRUMap(500));
    private Map<String, Player> _playerByName = Collections.synchronizedMap(new LRUMap(500));

    public CachedPlayerDAO(PlayerDAO delegate) {
        _delegate = delegate;
    }

    @Override
    public void clearCache() {
        _playerById.clear();
        _playerByName.clear();
    }

    @Override
    public int getItemCount() {
        return _playerById.size() + _playerByName.size();
    }

    @Override
    public Player getPlayer(int id) {
        Player player = (Player) _playerById.get(id);
        if (player == null) {
            player = _delegate.getPlayer(id);
            if (player != null) {
                _playerById.put(id, player);
                _playerByName.put(player.getName(), player);
            }
        }
        return player;
    }

    @Override
    public Player getPlayer(String playerName) {
        Player player = (Player) _playerByName.get(playerName);
        if (player == null) {
            player = _delegate.getPlayer(playerName);
            if (player != null) {
                _playerById.put(player.getId(), player);
                _playerByName.put(player.getName(), player);
            }
        }
        return player;
    }

    @Override
    public Player loginUser(String login, String password) throws SQLException {
        return _delegate.loginUser(login, password);
    }

    @Override
    public boolean registerUser(String login, String password, String remoteAddr) throws SQLException, LoginInvalidException {
        boolean registered = _delegate.registerUser(login, password, remoteAddr);
        if (registered)
            _playerByName.remove(login);
        return registered;
    }

    @Override
    public void setLastReward(Player player, int currentReward) throws SQLException {
        _delegate.setLastReward(player, currentReward);
        _playerById.remove(player.getId());
        _playerByName.remove(player.getName());
    }

    @Override
    public void updateLastLoginIp(String login, String remoteAddr) throws SQLException {
        _delegate.updateLastLoginIp(login, remoteAddr);
    }

    @Override
    public boolean updateLastReward(Player player, int previousReward, int currentReward) throws SQLException {
        boolean updated = _delegate.updateLastReward(player, previousReward, currentReward);
        if (updated) {
            _playerById.remove(player.getId());
            _playerByName.remove(player.getName());
        }
        return updated;
    }
}
