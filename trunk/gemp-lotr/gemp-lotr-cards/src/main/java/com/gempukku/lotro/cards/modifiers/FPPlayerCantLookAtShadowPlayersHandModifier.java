package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class FPPlayerCantLookAtShadowPlayersHandModifier extends AbstractModifier {
    public FPPlayerCantLookAtShadowPlayersHandModifier(PhysicalCard source) {
        super(source, "The Free Peoples player may not look at or reveal cards in any Shadow player's hand", null, ModifierEffect.LOOK_OR_REVEAL_MODIFIER);
    }

    @Override
    public boolean canLookOrRevealCardsInHand(GameState gameState, ModifiersQuerying modifiersQuerying, String revealingPlayerId, String actingPlayerId) {
        if (actingPlayerId.equals(gameState.getCurrentPlayerId())
                && !revealingPlayerId.equals(gameState.getCurrentPlayerId()))
            return false;
        return true;
    }
}
