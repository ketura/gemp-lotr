package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class MoveLimitModifier extends AbstractModifier {
    private Condition _condition;
    private int _moveLimitModifier;

    public MoveLimitModifier(PhysicalCard source, int moveLimitModifier) {
        this(source, null, moveLimitModifier);
    }

    public MoveLimitModifier(PhysicalCard source, Condition condition, int moveLimitModifier) {
        super(source, "Move limit " + ((moveLimitModifier < 0) ? moveLimitModifier : ("+" + moveLimitModifier)), null, ModifierEffect.MOVE_LIMIT_MODIFIER);
        _condition = condition;
        _moveLimitModifier = moveLimitModifier;
    }

    @Override
    public int getMoveLimitModifier(GameState gameState, ModifiersQuerying modifiersQuerying) {
        if (_condition == null || _condition.isFullfilled(gameState, modifiersQuerying))
            return _moveLimitModifier;
        return 0;
    }
}
