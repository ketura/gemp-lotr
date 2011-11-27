package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class AssignmentResult extends EffectResult {
    private String _playerId;
    private Map<PhysicalCard, Set<PhysicalCard>> _assignments;

    public AssignmentResult(String playerId, Map<PhysicalCard, Set<PhysicalCard>> assignments) {
        super(EffectResult.Type.ASSIGNMENT);
        _playerId = playerId;
        _assignments = assignments;
    }

    public String getPlayerId() {
        return _playerId;
    }

    public Map<PhysicalCard, Set<PhysicalCard>> getAssignments() {
        return Collections.unmodifiableMap(_assignments);
    }
}
