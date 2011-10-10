package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AssignmentResult extends EffectResult {
    private String _playerId;
    private Map<PhysicalCard, List<PhysicalCard>> _assignments;

    public AssignmentResult(String playerId, Map<PhysicalCard, List<PhysicalCard>> assignments) {
        super(EffectResult.Type.ASSIGNMENT);
        _playerId = playerId;
        _assignments = assignments;
    }

    public String getPlayerId() {
        return _playerId;
    }

    public Map<PhysicalCard, List<PhysicalCard>> getAssignments() {
        return Collections.unmodifiableMap(_assignments);
    }
}
