package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class MoveLimitModifier extends AbstractModifier {
    private int _moveLimitModifier;

    public MoveLimitModifier(PhysicalCard source, String text, int moveLimitModifier) {
        super(source, text, null);
        _moveLimitModifier = moveLimitModifier;
    }

    @Override
    public int getMoveLimit(GameState gameState, ModifiersQuerying modifiersQuerying, int result) {
        return result + _moveLimitModifier;
    }
}
