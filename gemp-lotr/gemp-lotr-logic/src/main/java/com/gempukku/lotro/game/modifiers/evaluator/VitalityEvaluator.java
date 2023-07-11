package com.gempukku.lotro.game.modifiers.evaluator;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class VitalityEvaluator implements Evaluator {
    private final int _multiplier;
    private final PhysicalCard _physicalCard;

    public VitalityEvaluator(PhysicalCard physicalCard) {
        this(physicalCard, 1);
    }

    public VitalityEvaluator(PhysicalCard physicalCard, int multiplier) {
        _physicalCard = physicalCard;
        _multiplier = multiplier;
    }

    @Override
    public int evaluateExpression(DefaultGame game, PhysicalCard cardAffected) {
        return _multiplier * game.getModifiersQuerying().getVitality(game, _physicalCard);
    }
}
