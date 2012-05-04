package com.gempukku.lotro.league;

import com.gempukku.lotro.db.LeagueMatchDAO;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueMatchResult;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CachedLeagueMatchDAO implements LeagueMatchDAO {
    private LeagueMatchDAO _leagueMatchDAO;
    private ReadWriteLock _readWriteLock = new ReentrantReadWriteLock();

    private Map<String, Collection<LeagueMatchResult>> _cachedMatches = new ConcurrentHashMap<String, Collection<LeagueMatchResult>>();

    public CachedLeagueMatchDAO(LeagueMatchDAO leagueMatchDAO) {
        _leagueMatchDAO = leagueMatchDAO;
    }

    @Override
    public Collection<LeagueMatchResult> getLeagueMatches(League league) {
        _readWriteLock.readLock().lock();
        try {
            Collection<LeagueMatchResult> leagueMatches = _cachedMatches.get(LeagueMapKeys.getLeagueMapKey(league));
            if (leagueMatches == null) {
                _readWriteLock.readLock().unlock();
                _readWriteLock.writeLock().lock();
                try {
                    leagueMatches = getLeagueMatchesInWriteLock(league);
                } finally {
                    _readWriteLock.readLock().lock();
                    _readWriteLock.writeLock().unlock();
                }
            }
            return Collections.unmodifiableCollection(leagueMatches);
        } finally {
            _readWriteLock.readLock().unlock();
        }
    }

    private Collection<LeagueMatchResult> getLeagueMatchesInWriteLock(League league) {
        Collection<LeagueMatchResult> leagueMatches;
        leagueMatches = _cachedMatches.get(LeagueMapKeys.getLeagueMapKey(league));
        if (leagueMatches == null) {
            leagueMatches = new CopyOnWriteArraySet<LeagueMatchResult>(_leagueMatchDAO.getLeagueMatches(league));
            _cachedMatches.put(LeagueMapKeys.getLeagueMapKey(league), leagueMatches);
        }
        return leagueMatches;
    }

    @Override
    public void addPlayedMatch(League league, LeagueSerieData leagueSerie, String winner, String loser) {
        _readWriteLock.writeLock().lock();
        try {
            LeagueMatchResult match = new LeagueMatchResult(leagueSerie.getName(), winner, loser);

            getLeagueMatchesInWriteLock(league).add(match);
            _leagueMatchDAO.addPlayedMatch(league, leagueSerie, winner, loser);
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }

    public void clearCache() {
        _readWriteLock.writeLock().lock();
        try {
            _cachedMatches.clear();
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }
}
