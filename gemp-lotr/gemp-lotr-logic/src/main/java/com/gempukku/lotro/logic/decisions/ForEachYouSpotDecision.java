package com.gempukku.lotro.logic.decisions;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.LotroGame;

import java.util.Map;

public abstract class ForEachYouSpotDecision extends IntegerAwaitingDecision {
    private LotroGame _lotroGame;
    private Filterable[] _filters;
    private int _defaultValue;
    private int _max;

    protected ForEachYouSpotDecision(int id, String text, LotroGame lotroGame, int defaultValue, Filterable... filter) {
        super(id, text, 0);
        _lotroGame = lotroGame;
        _filters = filter;
        _defaultValue = defaultValue;
    }

    @Override
    public Map<String, Object> getDecisionParameters() {
        Map<String, Object> result = super.getDecisionParameters();
        int count = Filters.countActive(_lotroGame, _filters);
        if (_filters.length == 1)
            count += _lotroGame.getModifiersQuerying().getSpotBonus(_lotroGame, _filters[0]);
        _max = count;
        result.put("max", String.valueOf(count));
        if (_defaultValue > _max)
            _defaultValue = _max;
        result.put("defaultValue", String.valueOf(_defaultValue));
        return result;
    }

    @Override
    protected int getValidatedResult(String result) throws DecisionResultInvalidException {
        int value = super.getValidatedResult(result);
        if (_max < value)
            throw new DecisionResultInvalidException();

        return value;
    }
}
