package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.ArrayList;
import java.util.List;

public class DefaultCostToEffectAction implements CostToEffectAction {
    private PhysicalCard _physicalCard;
    private Keyword _type;
    private String _text;

    private List<Effect> _costs = new ArrayList<Effect>();
    private List<Effect> _effects = new ArrayList<Effect>();

    private int _costsNextIndex = 0;
    private int _effectsNextIndex = 0;

    public DefaultCostToEffectAction(PhysicalCard physicalCard, Keyword type, String text) {
        _physicalCard = physicalCard;
        _type = type;
        _text = text;
    }

    @Override
    public Keyword getType() {
        return _type;
    }

    @Override
    public PhysicalCard getActionSource() {
        return _physicalCard;
    }

    @Override
    public String getText() {
        return _text;
    }

    public void addCost(Effect cost) {
        _costs.add(cost);
    }

    public void addEffect(Effect effect) {
        _effects.add(effect);
    }

    @Override
    public Effect nextEffect() {
        if (_costsNextIndex < _costs.size()) {
            _costsNextIndex++;
            return _costs.get(_costsNextIndex - 1);
        }

        if (checkNoFailedOrCancelledCost()) {
            if (_effectsNextIndex < _effects.size()) {
                _effectsNextIndex++;
                return _effects.get(_effectsNextIndex - 1);
            }
        }

        return null;
    }

    private boolean checkNoFailedOrCancelledCost() {
        for (Effect cost : _costs) {
            if (cost.isCancelled() || cost.isFailed())
                return false;
        }
        return true;
    }
}
