package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.effects.KillEffect;
import com.gempukku.lotro.game.effects.EffectResult;

import java.util.Set;

public class KilledResult extends EffectResult {
    private final Set<LotroPhysicalCard> _killedCards;
    private final KillEffect.Cause _cause;

    public KilledResult(Set<LotroPhysicalCard> killedCards, KillEffect.Cause cause) {
        super(EffectResult.Type.ANY_NUMBER_KILLED);
        _killedCards = killedCards;
        _cause = cause;
    }

    public Set<LotroPhysicalCard> getKilledCards() {
        return _killedCards;
    }

    public KillEffect.Cause getCause() {
        return _cause;
    }
}
