package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.AbstractModifier;
import com.gempukku.lotro.modifiers.ModifierEffect;
import com.gempukku.lotro.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.modifiers.evaluator.Evaluator;

public class MoveLimitModifier extends AbstractModifier {
    private final Evaluator _moveLimitModifier;

    public MoveLimitModifier(LotroPhysicalCard source, int moveLimitModifier) {
        super(source, null, null, null, ModifierEffect.MOVE_LIMIT_MODIFIER);
        _moveLimitModifier = new ConstantEvaluator(moveLimitModifier);
    }

    @Override
    public int getMoveLimitModifier(DefaultGame game) {
        return _moveLimitModifier.evaluateExpression(game, null);
    }
}
