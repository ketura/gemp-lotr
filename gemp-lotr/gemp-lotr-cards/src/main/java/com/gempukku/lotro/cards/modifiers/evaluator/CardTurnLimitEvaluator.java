package com.gempukku.lotro.cards.modifiers.evaluator;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.LimitCounter;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class CardTurnLimitEvaluator implements Evaluator {
    private Integer _evaluated;

    private Evaluator _evaluator;

    private LotroGame _game;
    private PhysicalCard _source;
    private int _limit;

    public CardTurnLimitEvaluator(LotroGame game, PhysicalCard source, int limit, Evaluator evaluator) {
        _game = game;
        _source = source;
        _limit = limit;
        _evaluator = evaluator;
    }

    private int evaluateOnce(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
        LimitCounter limitCounter = modifiersQuerying.getUntilEndOfTurnLimitCounter(_source);
        int internalResult = _evaluator.evaluateExpression(gameState, modifiersQuerying, cardAffected);
        return limitCounter.incrementToLimit(_limit, internalResult);
    }

    @Override
    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
        if (_evaluated == null)
            _evaluated = evaluateOnce(gameState, modifiersQuerying, cardAffected);
        return _evaluated;
    }
}