package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.league.LeagueSerieData;

import java.util.Map;

public interface LeaguePointsDAO {
    public void addPoints(League league, LeagueSerieData serie, String playerName, int points);

    public Map<String, Points> getLeaguePoints(League league);

    public Map<String, Points> getLeagueSeriePoints(League league, LeagueSerieData serie);

    public static class Points {
        private final int _points;
        private final int _gamesPlayed;

        public Points(int points, int gamesPlayed) {
            _points = points;
            _gamesPlayed = gamesPlayed;
        }

        public int getPoints() {
            return _points;
        }

        public int getGamesPlayed() {
            return _gamesPlayed;
        }
    }
}
