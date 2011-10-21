package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.cards.decisions.ForEachTwilightTokenYouSpotDecision;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

public abstract class ForEachTwilightTokenYouSpotEffect implements Effect {
    private String _playerId;

    protected ForEachTwilightTokenYouSpotEffect(String playerId) {
        _playerId = playerId;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
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
        return null;
    }

    protected abstract void twilightTokensSpotted(int twilightTokens);

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }

    @Override
    public boolean wasSuccessful() {
        return true;
    }

    @Override
    public boolean wasCarriedOut() {
        return true;
    }

    @Override
    public void reset() {

    }
}