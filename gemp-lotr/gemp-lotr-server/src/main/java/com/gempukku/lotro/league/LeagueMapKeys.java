package com.gempukku.lotro.league;

import com.gempukku.lotro.db.vo.League;

public class LeagueMapKeys {
    public static String getLeagueMapKey(League league) {
        return league.getType();
    }

    public static String getLeagueSerieMapKey(League league, LeagueSerieData leagueSerie) {
        return league.getType() + ":" + leagueSerie.getName();
    }
}
