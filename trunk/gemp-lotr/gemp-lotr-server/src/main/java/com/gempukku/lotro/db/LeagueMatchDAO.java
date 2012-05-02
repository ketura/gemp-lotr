package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueMatch;
import com.gempukku.lotro.league.LeagueSerieData;

import java.util.Collection;

public interface LeagueMatchDAO {
    public Collection<LeagueMatch> getLeagueMatches(League league);

    public Collection<LeagueMatch> getLeagueSerieMatches(League league, LeagueSerieData leagueSerie);

    public void addPlayedMatch(League league, LeagueSerieData leagueSeason, LeagueMatch match);
}
