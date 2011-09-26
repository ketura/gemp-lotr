package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;

public class PlayEventEffect extends PlayCardEffect {
    private boolean _cancelled;

    public PlayEventEffect(PhysicalCard cardPlayed) {
        super(cardPlayed);
    }

    public void cancel() {
        _cancelled = true;
    }

    public boolean isCancelled() {
        return _cancelled;
    }
}
