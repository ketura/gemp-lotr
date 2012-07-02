package com.gempukku.lotro.tournament;

public interface TournamentTask {
    public void executeTask(TournamentCallback tournamentCallback);

    public long getExecuteAfter();
}
