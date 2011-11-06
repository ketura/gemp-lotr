package com.gempukku.lotro.cards.modifiers.evaluator;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

import java.util.HashSet;
import java.util.Set;

public class CountCulturesEvaluator implements Evaluator {
    private Filterable[] _filters;
    private int _multiplier;
    private int _over;

    public CountCulturesEvaluator(Filterable... filters) {
        this(0, filters);
    }

    public CountCulturesEvaluator(int over, Filterable... filters) {
        this(over, 1, filters);
    }

    public CountCulturesEvaluator(int over, int multiplier, Filterable... filters) {
        _over = over;
        _multiplier = multiplier;
        _filters = filters;
    }

    @Override
    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        Set<Culture> cultures = new HashSet<Culture>();
        for (PhysicalCard physicalCard : Filters.filterActive(gameState, modifiersQuerying, _filters))
            cultures.add(physicalCard.getBlueprint().getCulture());
        return _multiplier * Math.max(0, cultures.size() - _over);
    }
}
