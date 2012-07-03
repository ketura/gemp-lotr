package com.gempukku.lotro.tournament;

import java.util.Date;
import java.util.List;

public interface TournamentDAO {
    public void addTournament(String tournamentId, String className, String parameters, Date start);

    public void markTournamentFinished(String tournamentId);

    public List<TournamentInfo> getUnfinishedTournaments();

    public TournamentInfo getTournamentById(String tournamentId);
}
