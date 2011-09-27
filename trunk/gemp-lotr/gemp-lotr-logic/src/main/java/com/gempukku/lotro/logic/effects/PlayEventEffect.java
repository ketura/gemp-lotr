package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;

public class PlayEventEffect extends PlayCardEffect {
    private boolean _cancelled;
    private boolean _requiresRanger;

    public PlayEventEffect(PhysicalCard cardPlayed) {
        super(cardPlayed);
    }

    public boolean isRequiresRanger() {
        return _requiresRanger;
    }

    public void setRequiresRanger(boolean requiresRanger) {
        _requiresRanger = requiresRanger;
    }

    public void cancel() {
        _cancelled = true;
    }

    public boolean isCancelled() {
        return _cancelled;
    }
}
