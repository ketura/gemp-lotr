package com.gempukku.lotro.draft;

import com.gempukku.lotro.game.CardCollection;

public interface DraftCardChoice {
    public long getTimeLeft();

    public CardCollection getCardCollection();
}
