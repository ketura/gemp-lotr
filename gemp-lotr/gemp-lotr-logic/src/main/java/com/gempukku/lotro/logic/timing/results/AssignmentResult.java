package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.Set;

public class AssignmentResult extends EffectResult {
    private String _playerId;
    private PhysicalCard _assignedCard;
    private Set<PhysicalCard> _against;

    public AssignmentResult(String playerId, PhysicalCard assignedCard, PhysicalCard against) {
        super(EffectResult.Type.CHARACTER_ASSIGNED);
        _playerId = playerId;
        _assignedCard = assignedCard;
        _against = Collections.singleton(against);
    }

    public AssignmentResult(String playerId, PhysicalCard assignedCard, Set<PhysicalCard> against) {
        super(EffectResult.Type.CHARACTER_ASSIGNED);
        _playerId = playerId;
        _assignedCard = assignedCard;
        _against = against;
    }

    public String getPlayerId() {
        return _playerId;
    }

    public Set<PhysicalCard> getAgainst() {
        return _against;
    }

    public PhysicalCard getAssignedCard() {
        return _assignedCard;
    }
}

