package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class OpponentsCantLookOrRevealCardsFromHand extends AbstractModifier {
    private String _playerId;

    public OpponentsCantLookOrRevealCardsFromHand(PhysicalCard source, String playerId) {
        super(source, "Opponents can't look or reveal cards from hand", null, new ModifierEffect[]{ModifierEffect.LOOK_OR_REVEAL_MODIFIER});
        _playerId = playerId;
    }

    @Override
    public boolean canLookOrRevealCardsInHand(GameState gameState, ModifiersQuerying modifiersQuerying, String playerId) {
        if (_playerId.equals(playerId))
            return false;
        return true;
    }
}
