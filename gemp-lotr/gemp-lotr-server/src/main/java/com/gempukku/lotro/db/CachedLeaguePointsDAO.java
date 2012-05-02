package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.league.LeagueSerieData;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CachedLeaguePointsDAO implements LeaguePointsDAO {
    private LeaguePointsDAO _leaguePointsDAO;
    private Map<String, Map<String, Points>> _cachedPoints = new ConcurrentHashMap<String, Map<String, Points>>();

    private ReadWriteLock _readWriteLock = new ReentrantReadWriteLock();

    public CachedLeaguePointsDAO(LeaguePointsDAO leaguePointsDAO) {
        _leaguePointsDAO = leaguePointsDAO;
    }

    private String getLeagueCacheKey(League league) {
        return league.getType();
    }

    private String getLeagueSerieCacheKey(League league, LeagueSerieData leagueSerie) {
        int serieIndex = league.getLeagueData().getSeries().indexOf(leagueSerie);
        return league.getType() + "-serieIndex:" + serieIndex;
    }

    @Override
    public void addPoints(League league, LeagueSerieData serie, String playerName, int points) {
        _readWriteLock.writeLock().lock();
        try {
            Map<String, Points> leaguePointsInWriteLock = getLeaguePointsInWriteLock(league);
            Points previousPointsInLeague = leaguePointsInWriteLock.get(playerName);
            Points newLeaguePoints = new Points(
                    points + ((previousPointsInLeague != null) ? previousPointsInLeague.getPoints() : 0),
                    1 + ((previousPointsInLeague != null) ? previousPointsInLeague.getGamesPlayed() : 0));
            leaguePointsInWriteLock.put(playerName, newLeaguePoints);

            Map<String, Points> leagueSeriePointsInWriteLock = getLeagueSeriePointsInWriteLock(league, serie);
            Points previousPointsInLeagueSerie = leagueSeriePointsInWriteLock.get(playerName);
            Points newSeriePoints = new Points(
                    points + ((previousPointsInLeagueSerie != null) ? previousPointsInLeagueSerie.getPoints() : 0),
                    1 + ((previousPointsInLeagueSerie != null) ? previousPointsInLeagueSerie.getGamesPlayed() : 0));
            leagueSeriePointsInWriteLock.put(playerName, newSeriePoints);

            _leaguePointsDAO.addPoints(league, serie, playerName, points);
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public Map<String, Points> getLeaguePoints(League league) {
        _readWriteLock.readLock().lock();
        try {
            Map<String, Points> leaguePoints = _cachedPoints.get(getLeagueCacheKey(league));
            if (leaguePoints == null) {
                _readWriteLock.readLock().unlock();
                _readWriteLock.writeLock().lock();
                try {
                    leaguePoints = getLeaguePointsInWriteLock(league);
                } finally {
                    _readWriteLock.readLock().lock();
                    _readWriteLock.writeLock().unlock();
                }
            }
            return Collections.unmodifiableMap(leaguePoints);
        } finally {
            _readWriteLock.readLock().unlock();
        }
    }

    @Override
    public Map<String, Points> getLeagueSeriePoints(League league, LeagueSerieData serie) {
        _readWriteLock.readLock().lock();
        try {
            Map<String, Points> leagueMatches = _cachedPoints.get(getLeagueCacheKey(league));
            if (leagueMatches == null) {
                _readWriteLock.readLock().unlock();
                _readWriteLock.writeLock().lock();
                try {
                    leagueMatches = getLeaguePointsInWriteLock(league);
                } finally {
                    _readWriteLock.readLock().lock();
                    _readWriteLock.writeLock().unlock();
                }
            }
            return Collections.unmodifiableMap(leagueMatches);
        } finally {
            _readWriteLock.readLock().unlock();
        }
    }

    private Map<String, Points> getLeaguePointsInWriteLock(League league) {
        Map<String, Points> leaguePoints;
        leaguePoints = _cachedPoints.get(getLeagueCacheKey(league));
        if (leaguePoints == null) {
            leaguePoints = new ConcurrentHashMap<String, Points>(_leaguePointsDAO.getLeaguePoints(league));
            _cachedPoints.put(getLeagueCacheKey(league), leaguePoints);
        }
        return leaguePoints;
    }

    private Map<String, Points> getLeagueSeriePointsInWriteLock(League league, LeagueSerieData leagueSerie) {
        Map<String, Points> leagueSeriePoints;
        leagueSeriePoints = _cachedPoints.get(getLeagueSerieCacheKey(league, leagueSerie));
        if (leagueSeriePoints == null) {
            leagueSeriePoints = new ConcurrentHashMap<String, Points>(_leaguePointsDAO.getLeagueSeriePoints(league, leagueSerie));
            _cachedPoints.put(getLeagueSerieCacheKey(league, leagueSerie), leagueSeriePoints);
        }
        return leagueSeriePoints;
    }

    public void clearCache() {
        _readWriteLock.writeLock().lock();
        try {
            _cachedPoints.clear();
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }
}
