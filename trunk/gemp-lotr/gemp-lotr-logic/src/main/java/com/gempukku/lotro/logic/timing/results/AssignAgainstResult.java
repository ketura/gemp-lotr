package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.Set;

public class AssignAgainstResult extends EffectResult {
    private String _playerId;
    private PhysicalCard _assignedCard;
    private Set<PhysicalCard> _against;

    public AssignAgainstResult(String playerId, PhysicalCard assignedCard, PhysicalCard against) {
        super(Type.ASSIGNED_AGAINST);
        _playerId = playerId;
        _assignedCard = assignedCard;
        _against = Collections.singleton(against);
    }

    public AssignAgainstResult(String playerId, PhysicalCard assignedCard, Set<PhysicalCard> against) {
        super(Type.ASSIGNED_AGAINST);
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

