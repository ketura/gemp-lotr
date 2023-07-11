package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class ShadowPlayersCantLookAtYourHandModifier extends AbstractModifier {
    private final String _revealingPlayerId;

    public ShadowPlayersCantLookAtYourHandModifier(PhysicalCard source, String revealingPlayerId) {
        super(source, "Shadow players may not look at or reveal cards in your hand", null, ModifierEffect.LOOK_OR_REVEAL_MODIFIER);
        _revealingPlayerId = revealingPlayerId;
    }

    @Override
    public boolean canLookOrRevealCardsInHand(DefaultGame game, String revealingPlayerId, String actingPlayerId) {
        if (_revealingPlayerId != null && _revealingPlayerId.equals(revealingPlayerId)
                && actingPlayerId != null && !actingPlayerId.equals(game.getGameState().getCurrentPlayerId()))
            return false;
        return true;
    }
}
