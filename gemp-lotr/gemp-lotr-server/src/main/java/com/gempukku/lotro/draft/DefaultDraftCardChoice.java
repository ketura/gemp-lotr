package com.gempukku.lotro.draft;

import com.gempukku.lotro.game.CardCollection;

public class DefaultDraftCardChoice implements DraftCardChoice {
    private final CardCollection _cardCollection;
    private final long _pickEnd;

    public DefaultDraftCardChoice(CardCollection cardCollection, long pickEnd) {
        _cardCollection = cardCollection;
        _pickEnd = pickEnd;
    }

    @Override
    public long getTimeLeft() {
        return _pickEnd - System.currentTimeMillis();
    }

    @Override
    public CardCollection getCardCollection() {
        return _cardCollection;
    }
}
