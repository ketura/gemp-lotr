package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.cards.decisions.ForEachBurdenYouSpotDecision;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

public abstract class ForEachBurdenYouSpotEffect implements Effect {
    private String _playerId;

    protected ForEachBurdenYouSpotEffect(String playerId) {
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
        return null;
    }

    protected abstract void burdensSpotted(int burdensSpotted);

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
