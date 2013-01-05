package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class OpponentsCantLookOrRevealCardsFromHand extends AbstractModifier {
    private String _revealingPlayerId;
    private String _actingPlayerId;

    public OpponentsCantLookOrRevealCardsFromHand(PhysicalCard source, String revealingPlayerId) {
        super(source, "Opponents can't look or reveal cards from hand", null, ModifierEffect.LOOK_OR_REVEAL_MODIFIER);
        _revealingPlayerId = revealingPlayerId;
    }

    public OpponentsCantLookOrRevealCardsFromHand(PhysicalCard source, String revealingPlayerId, String actingPlayerId) {
        this(source, revealingPlayerId);
        _actingPlayerId = actingPlayerId;
    }

    @Override
    public boolean canLookOrRevealCardsInHand(GameState gameState, ModifiersQuerying modifiersQuerying, String revealingPlayerId, String actingPlayerId) {
        if (_revealingPlayerId == null || _revealingPlayerId.equals(revealingPlayerId))
            return false;
        if (_actingPlayerId == null || _actingPlayerId.equals(actingPlayerId))
            return false;
        return true;
    }
}
