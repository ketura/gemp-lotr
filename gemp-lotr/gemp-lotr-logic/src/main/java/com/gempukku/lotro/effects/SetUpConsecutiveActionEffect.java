package com.gempukku.lotro.effects;

import com.gempukku.lotro.game.DefaultGame;

public class SetUpConsecutiveActionEffect extends AbstractSuccessfulEffect {
    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public void playEffect(DefaultGame game) {
        game.getGameState().setConsecutiveAction(true);
    }
}
