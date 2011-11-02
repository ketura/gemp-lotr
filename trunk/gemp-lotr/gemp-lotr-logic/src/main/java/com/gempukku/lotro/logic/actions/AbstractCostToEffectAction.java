package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Arrays;
import java.util.LinkedList;

public abstract class AbstractCostToEffectAction implements CostToEffectAction {
    private LinkedList<Effect> _costs = new LinkedList<Effect>();
    private LinkedList<Effect> _processedCosts = new LinkedList<Effect>();

    private LinkedList<Effect> _effects = new LinkedList<Effect>();

    private Phase _actionTimeword;
    private String _performingPlayer;

    @Override
    public Phase getActionTimeword() {
        return _actionTimeword;
    }

    @Override
    public void setActionTimeword(Phase phase) {
        _actionTimeword = phase;
    }

    @Override
    public void setPerformingPlayer(String playerId) {
        _performingPlayer = playerId;
    }

    @Override
    public String getPerformingPlayer() {
        return _performingPlayer;
    }

    @Override
    public final void appendCost(Effect cost) {
        _costs.add(cost);
    }

    @Override
    public final void appendEffect(Effect effect) {
        _effects.add(effect);
    }

    @Override
    public final void insertCost(Effect... cost) {
        _costs.addAll(0, Arrays.asList(cost));
    }

    @Override
    public final void insertEffect(Effect... effect) {
        _effects.addAll(0, Arrays.asList(effect));
    }

    protected boolean isCostFailed() {
        for (Effect processedCost : _processedCosts) {
            if (!processedCost.wasSuccessful())
                return true;
        }
        return false;
    }

    protected final Effect getNextCost() {
        Effect cost = _costs.poll();
        if (cost != null)
            _processedCosts.add(cost);
        return cost;
    }

    protected final Effect getNextEffect() {
        return _effects.poll();
    }
}
