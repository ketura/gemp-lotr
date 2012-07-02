package com.gempukku.lotro.tournament;

import java.util.List;

public interface TournamentMatchDAO {
    public void addMatch(String tournamentId, int round, String playerOne, String playerTwo);

    public void setMatchResult(String tournamentId, int round, String winner);

    public List<TournamentMatch> getMatches(String tournamentId, int round);
}
