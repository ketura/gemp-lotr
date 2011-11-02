package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;

public class SubAction implements Action {
    private Action _action;
    private LinkedList<Effect> _effects = new LinkedList<Effect>();
    private LinkedList<Effect> _processedEffects = new LinkedList<Effect>();

    public SubAction(Action action) {
        _action = action;
    }

    public void appendEffect(Effect effect) {
        _effects.add(effect);
    }

    @Override
    public PhysicalCard getActionSource() {
        return _action.getActionSource();
    }

    @Override
    public PhysicalCard getActionAttachedToCard() {
        return _action.getActionAttachedToCard();
    }

    @Override
    public Phase getActionTimeword() {
        return _action.getActionTimeword();
    }

    @Override
    public void setActionTimeword(Phase phase) {
        _action.setActionTimeword(phase);
    }

    @Override
    public String getPerformingPlayer() {
        return _action.getPerformingPlayer();
    }

    @Override
    public void setPerformingPlayer(String playerId) {
        _action.setPerformingPlayer(playerId);
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Effect nextEffect(LotroGame game) {
        final Effect effect = _effects.poll();
        if (effect != null)
            _processedEffects.add(effect);
        return effect;
    }

    public boolean wasSuccessful() {
        if (!_effects.isEmpty())
            return false;
        for (Effect effect : _processedEffects) {
            if (!effect.wasSuccessful())
                return false;
        }

        return true;
    }

    public boolean wasCarriedOut() {
        if (!_effects.isEmpty())
            return false;
        for (Effect effect : _processedEffects) {
            if (!effect.wasCarriedOut())
                return false;
        }

        return true;
    }
}
