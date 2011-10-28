package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.actions.PlayerReconcilesAction;

public class ReconcileHandEffect extends AbstractEffect {
    private String _playerId;

    public ReconcileHandEffect(String playerId) {
        _playerId = playerId;
    }

    @Override
    public String getText(LotroGame game) {
        return "Reconcile hand";
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        PlayerReconcilesAction action = new PlayerReconcilesAction(game, _playerId);
        game.getActionsEnvironment().addActionToStack(action);
        return new FullEffectResult(null, true, true);
    }
}
