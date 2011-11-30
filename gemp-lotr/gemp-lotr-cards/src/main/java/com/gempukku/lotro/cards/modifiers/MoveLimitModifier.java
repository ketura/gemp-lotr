package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class MoveLimitModifier extends AbstractModifier {
    private int _moveLimitModifier;

    public MoveLimitModifier(PhysicalCard source, int moveLimitModifier) {
        this(source, null, moveLimitModifier);
    }

    public MoveLimitModifier(PhysicalCard source, Condition condition, int moveLimitModifier) {
        super(source, "Move limit " + ((moveLimitModifier < 0) ? moveLimitModifier : ("+" + moveLimitModifier)), null, condition, ModifierEffect.MOVE_LIMIT_MODIFIER);
        _moveLimitModifier = moveLimitModifier;
    }

    @Override
    public int getMoveLimitModifier(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return _moveLimitModifier;
    }
}
