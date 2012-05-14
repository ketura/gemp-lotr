package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.LeagueMatchResult;

import java.util.Collection;

public interface LeagueMatchDAO {
    public Collection<LeagueMatchResult> getLeagueMatches(String leagueId);

    public void addPlayedMatch(String leagueId, String serieId, String winner, String loser);
}
