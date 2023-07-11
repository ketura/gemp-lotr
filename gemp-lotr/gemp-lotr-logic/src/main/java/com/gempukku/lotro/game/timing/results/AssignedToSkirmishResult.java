package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.timing.EffectResult;

public class AssignedToSkirmishResult extends EffectResult {
    private final PhysicalCard _assigned;
    private final String _playerId;

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
