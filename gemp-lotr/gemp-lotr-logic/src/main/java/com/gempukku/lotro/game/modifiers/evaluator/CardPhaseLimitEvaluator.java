package com.gempukku.lotro.game.modifiers.evaluator;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.LimitCounter;

public class CardPhaseLimitEvaluator implements Evaluator {
    private Integer _evaluated;

    private final Evaluator _amount;

    private final DefaultGame _game;
    private final PhysicalCard _source;
    private final Phase _phase;
    private final Evaluator _limit;

    public CardPhaseLimitEvaluator(DefaultGame game, PhysicalCard source, Phase phase, Evaluator limit, Evaluator amount) {
        _game = game;
        _source = source;
        _phase = phase;
        _limit = limit;
        _amount = amount;
    }

    private int evaluateOnce(DefaultGame game, PhysicalCard cardAffected) {
        LimitCounter limitCounter = game.getModifiersQuerying().getUntilEndOfPhaseLimitCounter(_source, _phase);
        int amountResult = _amount.evaluateExpression(game, cardAffected);
        int limitResult = _limit.evaluateExpression(game, cardAffected);
        return limitCounter.incrementToLimit(limitResult, amountResult);
    }

    @Override
    public int evaluateExpression(DefaultGame game, PhysicalCard cardAffected) {
        if (_evaluated == null)
            _evaluated = evaluateOnce(game, cardAffected);
        return _evaluated;
    }
}
