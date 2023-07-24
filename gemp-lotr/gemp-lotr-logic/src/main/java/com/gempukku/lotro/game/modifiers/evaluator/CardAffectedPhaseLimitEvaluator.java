package com.gempukku.lotro.game.modifiers.evaluator;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.LimitCounter;

import java.util.HashMap;
import java.util.Map;

public class CardAffectedPhaseLimitEvaluator implements Evaluator {
    private final Map<Integer, Integer> _evaluatedForCard = new HashMap<>();

    private final String prefix;
    private final Evaluator evaluator;

    private final LotroPhysicalCard source;
    private final Phase phase;
    private final int limit;

    public CardAffectedPhaseLimitEvaluator(LotroPhysicalCard source, Phase phase, int limit, String prefix, Evaluator evaluator) {
        this.source = source;
        this.phase = phase;
        this.limit = limit;
        this.prefix = prefix;
        this.evaluator = evaluator;
    }

    private int evaluateOnce(DefaultGame game, LotroPhysicalCard cardAffected) {
        LimitCounter limitCounter = game.getModifiersQuerying().getUntilEndOfPhaseLimitCounter(source, prefix + cardAffected.getCardId() + "_", phase);
        int internalResult = evaluator.evaluateExpression(game, cardAffected);
        return limitCounter.incrementToLimit(limit, internalResult);
    }

    @Override
    public int evaluateExpression(DefaultGame game, LotroPhysicalCard cardAffected) {
        Integer value = _evaluatedForCard.get(cardAffected.getCardId());
        if (value == null) {
            value = evaluateOnce(game, cardAffected);
            _evaluatedForCard.put(cardAffected.getCardId(), value);
        }
        return value;
    }
}
