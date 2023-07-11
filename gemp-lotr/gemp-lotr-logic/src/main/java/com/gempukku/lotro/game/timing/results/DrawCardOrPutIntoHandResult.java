package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.game.timing.EffectResult;

public class DrawCardOrPutIntoHandResult extends EffectResult {
    private final String _playerId;
    private final boolean _draw;

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
