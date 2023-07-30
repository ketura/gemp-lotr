package com.gempukku.lotro.effects.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.effects.EffectResult;

import java.util.Collections;
import java.util.Set;

public class AssignAgainstResult extends EffectResult {
    private final String _playerId;
    private final LotroPhysicalCard _assignedCard;
    private final Set<LotroPhysicalCard> _against;

    public AssignAgainstResult(String playerId, LotroPhysicalCard assignedCard, LotroPhysicalCard against) {
        super(Type.ASSIGNED_AGAINST);
        _playerId = playerId;
        _assignedCard = assignedCard;
        _against = Collections.singleton(against);
    }

    public AssignAgainstResult(String playerId, LotroPhysicalCard assignedCard, Set<LotroPhysicalCard> against) {
        super(Type.ASSIGNED_AGAINST);
        _playerId = playerId;
        _assignedCard = assignedCard;
        _against = against;
    }

    public String getPlayerId() {
        return _playerId;
    }

    public Set<LotroPhysicalCard> getAgainst() {
        return _against;
    }

    public LotroPhysicalCard getAssignedCard() {
        return _assignedCard;
    }
}

