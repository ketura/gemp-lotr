package com.gempukku.lotro.cards.modifiers.evaluator;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class CountStackedEvaluator implements Evaluator {
    private Filterable _stackedOn;
    private Filterable[] _stackedCard;
    private Integer _limit;

    public CountStackedEvaluator(Filterable stackedOn, Filterable... stackedCard) {
        _stackedOn = stackedOn;
        _stackedCard = stackedCard;
    }

    public CountStackedEvaluator(int limit, Filterable stackedOn, Filterable... stackedCard) {
        this(stackedOn, stackedCard);
        _limit = limit;
    }

    @Override
    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
        int count = 0;
        for (PhysicalCard card : Filters.filterActive(gameState, modifiersQuerying, _stackedOn)) {
            count += Filters.filter(gameState.getStackedCards(card), gameState, modifiersQuerying, _stackedCard).size();
        }
        if (_limit != null)
            return Math.min(_limit, count);
        return count;
    }
}
