package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class DrawCardEffect extends UnrespondableEffect {
    private String _playerId;
    private int _count;

    public DrawCardEffect(String playerId, int count) {
        _playerId = playerId;
        _count = count;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return game.getGameState().getDeck(_playerId).size() > 0;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        int drawn = 0;
        for (int i = 0; i < _count; i++) {
            if (game.getGameState().getDeck(_playerId).size() > 0 && game.getModifiersQuerying().canDrawCardAndIncrement(game.getGameState(), _playerId)) {
                game.getGameState().playerDrawsCard(_playerId);
                drawn++;
            }
        }
        game.getGameState().sendMessage(_playerId + " draws " + drawn + " card(s)");
    }
}
