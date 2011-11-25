package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.logic.timing.EffectResult;

public class DrawCardOrPutIntoHandResult extends EffectResult {
    private String _playerId;

    public DrawCardOrPutIntoHandResult(String playerId) {
        super(EffectResult.Type.DRAW_CARD_OR_PUT_INTO_HAND);
        _playerId = playerId;
    }

    public String getPlayerId() {
        return _playerId;
    }
}
