package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public class KillResult extends EffectResult {
    private List<PhysicalCard> _killedCards;

    public KillResult(List<PhysicalCard> killedCards) {
        super(EffectResult.Type.KILL);
        _killedCards = killedCards;
    }

    public List<PhysicalCard> getKilledCards() {
        return _killedCards;
    }
}
