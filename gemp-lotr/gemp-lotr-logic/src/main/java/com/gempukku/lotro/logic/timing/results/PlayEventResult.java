package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;

public class PlayEventResult extends PlayCardResult {
    private boolean _eventCancelled;
    private boolean _requiresRanger;
    private Zone _targetZone = Zone.DISCARD;

    public PlayEventResult(PhysicalCard playedCard, boolean requiresRanger) {
        super(playedCard, null);
        _requiresRanger = requiresRanger;
    }

    public void setTargetZone(Zone zone) {
        _targetZone = zone;
    }

    public Zone getTargetZone() {
        return _targetZone;
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
}
