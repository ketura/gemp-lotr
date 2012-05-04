package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueMatchResult;
import com.gempukku.lotro.league.LeagueSerieData;

import java.util.Collection;

public interface LeagueMatchDAO {
    public Collection<LeagueMatchResult> getLeagueMatches(League league);

    public void addPlayedMatch(League league, LeagueSerieData leagueSeason, String winner, String loser);
}
