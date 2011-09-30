package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.logic.timing.EffectResult;

public class DrawCardOrPutIntoHandResult extends EffectResult {
    private String _playerId;
    private int _count;

    public DrawCardOrPutIntoHandResult(String playerId, int count) {
        super(EffectResult.Type.DRAW_CARD_OR_PUT_INTO_HAND);
        _playerId = playerId;
        _count = count;
    }

    public String getPlayerId() {
        return _playerId;
    }

    public int getCount() {
        return _count;
    }
}
