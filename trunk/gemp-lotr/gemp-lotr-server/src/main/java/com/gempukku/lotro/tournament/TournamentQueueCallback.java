package com.gempukku.lotro.tournament;

import com.gempukku.lotro.draft.Draft;

public interface TournamentQueueCallback {
    public void createTournament(Tournament tournament);

    public void createDraft(Draft draft);
}
