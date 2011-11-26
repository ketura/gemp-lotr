package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class SendMessageEffect extends UnrespondableEffect {
    private String _message;

    public SendMessageEffect(String message) {
        _message = message;
    }

    @Override
    protected void doPlayEffect(LotroGame game) {
        if (_message != null)
            game.getGameState().sendMessage(_message);
    }
}
