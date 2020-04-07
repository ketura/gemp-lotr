package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.LimitCounter;

public class CardPhaseLimitEvaluator implements Evaluator {
    private Integer _evaluated;

    private Evaluator _evaluator;

    private LotroGame _game;
    private PhysicalCard _source;
    private Phase _phase;
    private int _limit;

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
