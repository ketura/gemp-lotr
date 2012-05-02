package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueMatch;
import com.gempukku.lotro.league.LeagueSerieData;

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

    private Map<String, Collection<LeagueMatch>> _cachedMatches = new ConcurrentHashMap<String, Collection<LeagueMatch>>();

    public CachedLeagueMatchDAO(LeagueMatchDAO leagueMatchDAO) {
        _leagueMatchDAO = leagueMatchDAO;
    }

    private String getLeagueCacheKey(League league) {
        return league.getType();
    }

    private String getLeagueSerieCacheKey(League league, LeagueSerieData leagueSerie) {
        int serieIndex = league.getLeagueData().getSeries().indexOf(leagueSerie);
        return league.getType() + "-serieIndex:" + serieIndex;
    }

    @Override
    public Collection<LeagueMatch> getLeagueMatches(League league) {
        _readWriteLock.readLock().lock();
        try {
            Collection<LeagueMatch> leagueMatches = _cachedMatches.get(getLeagueCacheKey(league));
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

    @Override
    public Collection<LeagueMatch> getLeagueSerieMatches(League league, LeagueSerieData leagueSerie) {
        _readWriteLock.readLock().lock();
        try {
            Collection<LeagueMatch> leagueSerieMatches = _cachedMatches.get(getLeagueSerieCacheKey(league, leagueSerie));
            if (leagueSerieMatches == null) {
                _readWriteLock.readLock().unlock();
                _readWriteLock.writeLock().lock();
                try {
                    leagueSerieMatches = getLeagueSerieMatchesInWriteLock(league, leagueSerie);
                } finally {
                    _readWriteLock.readLock().lock();
                    _readWriteLock.writeLock().unlock();
                }
            }
            return Collections.unmodifiableCollection(leagueSerieMatches);
        } finally {
            _readWriteLock.readLock().unlock();
        }
    }

    private Collection<LeagueMatch> getLeagueMatchesInWriteLock(League league) {
        Collection<LeagueMatch> leagueMatches;
        leagueMatches = _cachedMatches.get(getLeagueCacheKey(league));
        if (leagueMatches == null) {
            leagueMatches = new CopyOnWriteArraySet<LeagueMatch>(_leagueMatchDAO.getLeagueMatches(league));
            _cachedMatches.put(getLeagueCacheKey(league), leagueMatches);
        }
        return leagueMatches;
    }

    private Collection<LeagueMatch> getLeagueSerieMatchesInWriteLock(League league, LeagueSerieData leagueSerie) {
        Collection<LeagueMatch> leagueSerieMatches;
        leagueSerieMatches = _cachedMatches.get(getLeagueSerieCacheKey(league, leagueSerie));
        if (leagueSerieMatches == null) {
            leagueSerieMatches = new CopyOnWriteArraySet<LeagueMatch>(_leagueMatchDAO.getLeagueSerieMatches(league, leagueSerie));
            _cachedMatches.put(getLeagueSerieCacheKey(league, leagueSerie), leagueSerieMatches);
        }
        return leagueSerieMatches;
    }

    @Override
    public void addPlayedMatch(League league, LeagueSerieData leagueSeason, String winner, String loser) {
        _readWriteLock.writeLock().lock();
        try {
            LeagueMatch match = new LeagueMatch(winner, loser);

            getLeagueMatchesInWriteLock(league).add(match);
            getLeagueSerieMatchesInWriteLock(league, leagueSeason).add(match);
            _leagueMatchDAO.addPlayedMatch(league, leagueSeason, winner, loser);
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
