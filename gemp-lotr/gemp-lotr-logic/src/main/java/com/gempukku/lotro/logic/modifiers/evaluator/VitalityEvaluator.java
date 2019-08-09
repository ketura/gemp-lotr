package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class VitalityEvaluator implements Evaluator {
    private int _multiplier;
    private PhysicalCard _physicalCard;

    public VitalityEvaluator(PhysicalCard physicalCard) {
        this(physicalCard, 1);
    }

    public VitalityEvaluator(PhysicalCard physicalCard, int multiplier) {
        _physicalCard = physicalCard;
        _multiplier = multiplier;
    }

    @Override
    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
        return _multiplier * game.getModifiersQuerying().getVitality(game, _physicalCard);
    }
}
