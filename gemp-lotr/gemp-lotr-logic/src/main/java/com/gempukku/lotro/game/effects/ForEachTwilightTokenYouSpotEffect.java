package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.decisions.ForEachTwilightTokenYouSpotDecision;

public abstract class ForEachTwilightTokenYouSpotEffect implements Effect {
    private final String _playerId;

    protected ForEachTwilightTokenYouSpotEffect(String playerId) {
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
        int twilightPool = game.getGameState().getTwilightPool();
        if (twilightPool == 0)
            twilightTokensSpotted(0);
        else
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new ForEachTwilightTokenYouSpotDecision(1, twilightPool) {
                        @Override
                        protected void twilightTokensSpotted(int twilightTokensSpotted) {
                            ForEachTwilightTokenYouSpotEffect.this.twilightTokensSpotted(twilightTokensSpotted);
                        }
                    });
    }

    protected abstract void twilightTokensSpotted(int twilightTokens);

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return true;
    }

    @Override
    public boolean wasCarriedOut() {
        return true;
    }
}