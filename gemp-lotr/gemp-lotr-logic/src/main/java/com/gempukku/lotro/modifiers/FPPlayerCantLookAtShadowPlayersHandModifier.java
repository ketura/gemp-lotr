package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class FPPlayerCantLookAtShadowPlayersHandModifier extends AbstractModifier {
    public FPPlayerCantLookAtShadowPlayersHandModifier(LotroPhysicalCard source) {
        super(source, "The Free Peoples player may not look at or reveal cards in any Shadow player's hand", null, ModifierEffect.LOOK_OR_REVEAL_MODIFIER);
    }

    @Override
    public boolean canLookOrRevealCardsInHand(DefaultGame game, String revealingPlayerId, String actingPlayerId) {
        if (actingPlayerId.equals(game.getGameState().getCurrentPlayerId())
                && !revealingPlayerId.equals(game.getGameState().getCurrentPlayerId()))
            return false;
        return true;
    }
}
