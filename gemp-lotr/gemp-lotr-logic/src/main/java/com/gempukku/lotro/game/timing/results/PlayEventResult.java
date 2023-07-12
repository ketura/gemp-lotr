package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.actions.lotronly.PlayEventAction;

public class PlayEventResult extends PlayCardResult {
    private boolean _eventCancelled;
    private final PlayEventAction _action;
    private final boolean _requiresRanger;

    public PlayEventResult(PlayEventAction action, Zone playedFrom, PhysicalCard playedCard, boolean requiresRanger, boolean paidToil) {
        super(playedFrom, playedCard, null, null, paidToil);
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
