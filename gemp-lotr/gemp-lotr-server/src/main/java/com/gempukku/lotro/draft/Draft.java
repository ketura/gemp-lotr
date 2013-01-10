package com.gempukku.lotro.draft;

import com.gempukku.lotro.SubscriptionConflictException;
import com.gempukku.lotro.SubscriptionExpiredException;
import com.gempukku.lotro.tournament.TournamentCallback;

public interface Draft {
    public void advanceDraft(TournamentCallback draftCallback);

    public void playerChosenCard(String playerName, String cardId);

    public void signUpForDraft(String playerName, DraftChannelVisitor draftChannelVisitor);
    public boolean hasChanges(String playerName, int channelNumber) throws SubscriptionExpiredException, SubscriptionConflictException;
    public void processDraft(String playerName, int channelNumber, DraftChannelVisitor draftChannelVisitor) throws SubscriptionExpiredException, SubscriptionConflictException;

    public boolean isFinished();
}
