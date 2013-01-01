package com.gempukku.lotro.tournament;

import com.gempukku.lotro.collection.CollectionsManager;

public interface TournamentTask {
    public void executeTask(TournamentCallback tournamentCallback, CollectionsManager collectionsManager);

    public long getExecuteAfter();
}
