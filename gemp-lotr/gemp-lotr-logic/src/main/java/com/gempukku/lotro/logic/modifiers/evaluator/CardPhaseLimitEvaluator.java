package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.LimitCounter;

public class CardPhaseLimitEvaluator implements Evaluator {
    private Integer _evaluated;

    private final Evaluator _evaluator;

    private final LotroGame _game;
    private final PhysicalCard _source;
    private final Phase _phase;
    private final int _limit;

    public CardPhaseLimitEvaluator(LotroGame game, PhysicalCard source, Phase phase, int limit, Evaluator evaluator) {
        _game = game;
        _source = source;
        _phase = phase;
        _limit = limit;
        _evaluator = evaluator;
    }

    private int evaluateOnce(LotroGame game, PhysicalCard cardAffected) {
        LimitCounter limitCounter = game.getModifiersQuerying().getUntilEndOfPhaseLimitCounter(_source, _phase);
        int internalResult = _evaluator.evaluateExpression(game, cardAffected);
        return limitCounter.incrementToLimit(_limit, internalResult);
    }

    @Override
    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
        if (_evaluated == null)
            _evaluated = evaluateOnce(game, cardAffected);
        return _evaluated;
    }
}
