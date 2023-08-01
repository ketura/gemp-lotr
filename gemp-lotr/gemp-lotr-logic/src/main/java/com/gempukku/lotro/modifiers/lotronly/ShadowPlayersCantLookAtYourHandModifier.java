package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.AbstractModifier;
import com.gempukku.lotro.modifiers.ModifierEffect;

public class ShadowPlayersCantLookAtYourHandModifier extends AbstractModifier {
    private final String _revealingPlayerId;

    public ShadowPlayersCantLookAtYourHandModifier(LotroPhysicalCard source, String revealingPlayerId) {
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
