package com.gempukku.lotro.draft;

import com.gempukku.lotro.game.CardCollection;

public interface DraftChannelVisitor {
    public void channelNumber(int channelNumber);
    public void timeLeft(long timeLeft);
    public void cardChoice(CardCollection cardCollection);
    public void noCardChoice();
    public void chosenCards(CardCollection cardCollection);
}
