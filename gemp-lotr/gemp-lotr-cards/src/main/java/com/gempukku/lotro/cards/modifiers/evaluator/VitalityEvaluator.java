package com.gempukku.lotro.cards.modifiers.evaluator;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

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
    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
        return _multiplier * modifiersQuerying.getVitality(gameState, _physicalCard);
    }
}
