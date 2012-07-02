package com.gempukku.lotro.tournament;

public interface TournamentQueueCallback {
    public void createTournament(Tournament tournament);

    public void createTournamentQueue(TournamentQueue tournamentQueue);
}
