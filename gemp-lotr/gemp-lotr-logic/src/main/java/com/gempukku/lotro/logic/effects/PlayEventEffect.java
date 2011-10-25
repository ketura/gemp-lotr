package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.results.PlayEventResult;

import java.util.Collections;

public class PlayEventEffect extends PlayCardEffect {
    private PhysicalCard _cardPlayed;
    private boolean _requiresRanger;
    private PlayEventResult _playEventResult;

    public PlayEventEffect(PhysicalCard cardPlayed, boolean requiresRanger) {
        super(cardPlayed);
        _cardPlayed = cardPlayed;
        _requiresRanger = requiresRanger;
        _playEventResult = new PlayEventResult(getPlayedCard(), _requiresRanger);
    }

    public PlayEventResult getPlayEventResult() {
        return _playEventResult;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        return new FullEffectResult(Collections.singleton(_playEventResult), true, true);
    }
}
