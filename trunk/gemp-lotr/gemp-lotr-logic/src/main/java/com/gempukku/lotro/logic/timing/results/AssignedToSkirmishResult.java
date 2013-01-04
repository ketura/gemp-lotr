package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class AssignedToSkirmishResult extends EffectResult {
    private PhysicalCard _assigned;
    private String _playerId;

    public AssignedToSkirmishResult(PhysicalCard assigned, String playerId) {
        super(Type.ASSIGNED_TO_SKIRMISH);
        _assigned = assigned;
        _playerId = playerId;
    }

    public PhysicalCard getAssignedCard() {
        return _assigned;
    }

    public String getPlayerId() {
        return _playerId;
    }
}
