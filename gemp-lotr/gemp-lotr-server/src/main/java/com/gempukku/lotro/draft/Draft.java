package com.gempukku.lotro.draft;

import com.gempukku.lotro.tournament.TournamentCallback;

public interface Draft {
    public void advanceDraft(TournamentCallback draftCallback);

    public void playerChosenCard(String playerName, String cardId);

    public DraftCardChoice getCardChoice(String playerName);

    public boolean isFinished();
}
