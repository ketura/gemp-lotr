package com.gempukku.lotro.cards.modifiers.evaluator;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.LimitCounter;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class CardLimitEvaluator implements Evaluator {
    private Integer _evaluated;

    private Evaluator _evaluator;

    private LotroGame _game;
    private PhysicalCard _source;
    private Phase _phase;
    private int _limit;

    public CardLimitEvaluator(LotroGame game, PhysicalCard source, Phase phase, int limit, Evaluator evaluator) {
        _game = game;
        _source = source;
        _phase = phase;
        _limit = limit;
        _evaluator = evaluator;
    }

    private int evaluateOnce(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
        LimitCounter limitCounter = _game.getModifiersEnvironment().getUntilEndOfPhaseLimitCounter(_source, _phase);
        int internalResult = _evaluator.evaluateExpression(gameState, modifiersQuerying, cardAffected);
        int availableBonusWithLimit = 0;
        for (int i = 0; i < internalResult; i++) {
            int limitResult = limitCounter.incrementCounter();
            if (limitResult <= _limit)
                availableBonusWithLimit++;
        }
        return availableBonusWithLimit;
    }

    @Override
    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
        if (_evaluated == null)
            _evaluated = evaluateOnce(gameState, modifiersQuerying, cardAffected);
        return _evaluated;
    }
}
