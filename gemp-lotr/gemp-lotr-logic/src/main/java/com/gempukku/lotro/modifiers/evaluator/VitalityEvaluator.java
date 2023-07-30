package com.gempukku.lotro.modifiers.evaluator;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class VitalityEvaluator implements Evaluator {
    private final int _multiplier;
    private final LotroPhysicalCard _physicalCard;

    public VitalityEvaluator(LotroPhysicalCard physicalCard) {
        this(physicalCard, 1);
    }

    public VitalityEvaluator(LotroPhysicalCard physicalCard, int multiplier) {
        _physicalCard = physicalCard;
        _multiplier = multiplier;
    }

    @Override
    public int evaluateExpression(DefaultGame game, LotroPhysicalCard cardAffected) {
        return _multiplier * game.getModifiersQuerying().getVitality(game, _physicalCard);
    }
}
