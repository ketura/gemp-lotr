package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class ShadowPlayersCantLookAtYourHandModifier extends AbstractModifier {
    private String _revealingPlayerId;

    public ShadowPlayersCantLookAtYourHandModifier(PhysicalCard source, String revealingPlayerId) {
        super(source, "Shadow players may not look at or reveal cards in your hand", null, ModifierEffect.LOOK_OR_REVEAL_MODIFIER);
        _revealingPlayerId = revealingPlayerId;
    }

    @Override
    public boolean canLookOrRevealCardsInHand(GameState gameState, ModifiersQuerying modifiersQuerying, String revealingPlayerId, String actingPlayerId) {
        if (_revealingPlayerId != null && _revealingPlayerId.equals(revealingPlayerId)
                && actingPlayerId != null && !actingPlayerId.equals(gameState.getCurrentPlayerId()))
            return false;
        return true;
    }
}
