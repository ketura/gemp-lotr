package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.UnrespondableEffect;

public class CorruptRingBearerEffect extends UnrespondableEffect {
    @Override
    public void doPlayEffect(DefaultGame game) {
        game.playerLost(game.getGameState().getCurrentPlayerId(), "The Ring-Bearer is corrupted by a card effect");
    }

    @Override
    public String getText(DefaultGame game) {
        return "Corrupt the Ring-Bearer";
    }
}
