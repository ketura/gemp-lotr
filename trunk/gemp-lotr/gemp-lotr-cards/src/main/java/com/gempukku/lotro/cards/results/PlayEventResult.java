package com.gempukku.lotro.cards.results;

import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

public class PlayEventResult extends PlayCardResult {
    private boolean _eventCancelled;
    private PlayEventAction _action;
    private boolean _requiresRanger;

    public PlayEventResult(PlayEventAction action, Zone playedFrom, PhysicalCard playedCard, boolean requiresRanger) {
        super(playedFrom, playedCard, null, null);
        _action = action;
        _requiresRanger = requiresRanger;
    }

    public boolean isRequiresRanger() {
        return _requiresRanger;
    }

    public void cancelEvent() {
        _eventCancelled = true;
    }

    public boolean isEventCancelled() {
        return _eventCancelled;
    }

    public PlayEventAction getPlayEventAction() {
        return _action;
    }
}
