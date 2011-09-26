package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Cost;
import com.gempukku.lotro.logic.timing.CostResolution;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class AbstractCostToEffectAction implements CostToEffectAction {
    private LinkedList<Cost> _costs = new LinkedList<Cost>();
    private Map<Cost, CostResolution> _processedCosts = new LinkedHashMap<Cost, CostResolution>();

    private LinkedList<Effect> _effects = new LinkedList<Effect>();
    private Map<Effect, EffectResult[]> _processedEffects = new LinkedHashMap<Effect, EffectResult[]>();

    @Override
    public final void appendCost(Cost cost) {
        _costs.add(cost);
    }

    @Override
    public final void appendEffect(Effect effect) {
        _effects.add(effect);
    }

    @Override
    public final void insertCost(Cost... cost) {
        _costs.addAll(0, Arrays.asList(cost));
    }

    @Override
    public final void insertEffect(Effect... effect) {
        _effects.addAll(0, Arrays.asList(effect));
    }

    protected boolean isCostFailed() {
        for (CostResolution resolution : _processedCosts.values()) {
            if (!resolution.isSuccessful())
                return true;
        }
        return false;
    }

    protected final Effect getNextCost() {
        Cost cost = _costs.poll();
        if (cost != null)
            return new EffectWrapper(cost);
        return null;
    }

    protected final Effect getNextEffect() {
        return _effects.poll();
    }

    private class EffectWrapper implements Effect {
        private Cost _delegate;

        private EffectWrapper(Cost delegate) {
            _delegate = delegate;
        }

        public String getText(LotroGame game) {
            return _delegate.getText(game);
        }

        public EffectResult.Type getType() {
            return _delegate.getType();
        }

        public EffectResult[] playEffect(LotroGame game) {
            CostResolution resolution = _delegate.playCost(game);
            _processedCosts.put(_delegate, resolution);
            return resolution.getEffectResults();
        }
    }
}
