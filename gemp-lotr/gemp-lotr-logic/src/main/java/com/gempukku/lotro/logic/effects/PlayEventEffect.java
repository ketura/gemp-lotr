package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayEventResult;

public class PlayEventEffect extends PlayCardEffect {
    private boolean _requiresRanger;
    private PlayEventResult _playEventResult;

    public PlayEventEffect(PhysicalCard cardPlayed, boolean requiresRanger) {
        super(cardPlayed);
        _requiresRanger = requiresRanger;
        _playEventResult = new PlayEventResult(getPlayedCard(), _requiresRanger);
    }

    public PlayEventResult getPlayEventResult() {
        return _playEventResult;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        return new FullEffectResult(new EffectResult[]{_playEventResult}, true, true);
    }
}
