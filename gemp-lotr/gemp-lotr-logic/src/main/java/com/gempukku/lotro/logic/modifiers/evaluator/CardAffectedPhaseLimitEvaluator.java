package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.LimitCounter;

import java.util.HashMap;
import java.util.Map;

public class CardAffectedPhaseLimitEvaluator implements Evaluator {
    private Map<Integer, Integer> _evaluatedForCard = new HashMap<>();

    private Evaluator _evaluator;

    private PhysicalCard _source;
    private Phase _phase;
    private int _limit;

    public CardAffectedPhaseLimitEvaluator(PhysicalCard source, Phase phase, int limit, Evaluator evaluator) {
        _source = source;
        _phase = phase;
        _limit = limit;
        _evaluator = evaluator;
    }

    private int evaluateOnce(LotroGame game, PhysicalCard cardAffected) {
        LimitCounter limitCounter = game.getModifiersQuerying().getUntilEndOfPhaseLimitCounter(_source, cardAffected.getCardId() + "_", _phase);
        int internalResult = _evaluator.evaluateExpression(game, cardAffected);
        return limitCounter.incrementToLimit(_limit, internalResult);
    }

    @Override
    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
        Integer value = _evaluatedForCard.get(cardAffected.getCardId());
        if (value == null) {
            value = evaluateOnce(game, cardAffected);
            _evaluatedForCard.put(cardAffected.getCardId(), value);
        }
        return value;
    }
}
