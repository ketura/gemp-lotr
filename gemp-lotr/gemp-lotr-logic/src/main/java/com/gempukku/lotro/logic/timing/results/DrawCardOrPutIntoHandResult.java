package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.logic.timing.EffectResult;

public class DrawCardOrPutIntoHandResult extends EffectResult {
    private String _playerId;
    private boolean _draw;

    public DrawCardOrPutIntoHandResult(String playerId) {
        this(playerId, false);
    }

    public DrawCardOrPutIntoHandResult(String playerId, boolean draw) {
        super(EffectResult.Type.DRAW_CARD_OR_PUT_INTO_HAND);
        _playerId = playerId;
        _draw = draw;
    }

    public String getPlayerId() {
        return _playerId;
    }

    public boolean isDraw() {
        return _draw;
    }
}
