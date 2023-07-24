package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.effects.KillEffect;
import com.gempukku.lotro.game.effects.EffectResult;

public class ForEachKilledResult extends EffectResult {
    private final LotroPhysicalCard _killedCard;
    private final KillEffect.Cause _cause;

    public ForEachKilledResult(LotroPhysicalCard killedCard, KillEffect.Cause cause) {
        super(EffectResult.Type.FOR_EACH_KILLED);
        _killedCard = killedCard;
        _cause = cause;
    }

    public LotroPhysicalCard getKilledCard() {
        return _killedCard;
    }

    public KillEffect.Cause getCause() {
        return _cause;
    }
}
