package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.decisions.ForEachBurdenYouSpotDecision;

public abstract class ForEachBurdenYouSpotEffect implements Effect {
    private final String _playerId;

    protected ForEachBurdenYouSpotEffect(String playerId) {
        _playerId = playerId;
    }

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
        int burdens = game.getGameState().getBurdens();
        if (burdens == 0)
            burdensSpotted(0);
        else
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new ForEachBurdenYouSpotDecision(1, burdens) {
                        @Override
                        protected void burdensSpotted(int burdensSpotted) {
                            ForEachBurdenYouSpotEffect.this.burdensSpotted(burdensSpotted);
                        }
                    });
    }

    protected abstract void burdensSpotted(int burdensSpotted);

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return true;
    }

    @Override
    public boolean wasCarriedOut() {
        return true;
    }
}
