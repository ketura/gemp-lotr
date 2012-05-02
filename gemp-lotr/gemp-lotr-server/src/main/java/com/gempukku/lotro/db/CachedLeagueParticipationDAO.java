package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CachedLeagueParticipationDAO implements LeagueParticipationDAO {
    private LeagueParticipationDAO _leagueParticipationDAO;
    private ReadWriteLock _readWriteLock = new ReentrantReadWriteLock();

    private Map<String, Set<String>> _cachedParticipants = new ConcurrentHashMap<String, Set<String>>();

    public CachedLeagueParticipationDAO(LeagueParticipationDAO leagueParticipationDAO) {
        _leagueParticipationDAO = leagueParticipationDAO;
    }

    private String getLeagueCacheKey(League league) {
        return league.getType();
    }

    @Override
    public void userJoinsLeague(League league, Player player) {
        _readWriteLock.writeLock().lock();
        try {
            getLeagueParticipantsInWriteLock(league).add(player.getName());
            _leagueParticipationDAO.userJoinsLeague(league, player);
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public Collection<String> getUsersParticipating(League league) {
        _readWriteLock.readLock().lock();
        try {
            Collection<String> leagueParticipants = _cachedParticipants.get(getLeagueCacheKey(league));
            if (leagueParticipants == null) {
                _readWriteLock.readLock().unlock();
                _readWriteLock.writeLock().lock();
                try {
                    leagueParticipants = getLeagueParticipantsInWriteLock(league);
                } finally {
                    _readWriteLock.readLock().lock();
                    _readWriteLock.writeLock().unlock();
                }
            }
            return Collections.unmodifiableCollection(leagueParticipants);
        } finally {
            _readWriteLock.readLock().unlock();
        }
    }

    private Collection<String> getLeagueParticipantsInWriteLock(League league) {
        Set<String> leagueParticipants;
        leagueParticipants = _cachedParticipants.get(getLeagueCacheKey(league));
        if (leagueParticipants == null) {
            leagueParticipants = new CopyOnWriteArraySet<String>(_leagueParticipationDAO.getUsersParticipating(league));
            _cachedParticipants.put(getLeagueCacheKey(league), leagueParticipants);
        }
        return leagueParticipants;
    }

    public void clearCache() {
        _readWriteLock.writeLock().lock();
        try {
            _cachedParticipants.clear();
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }
}
