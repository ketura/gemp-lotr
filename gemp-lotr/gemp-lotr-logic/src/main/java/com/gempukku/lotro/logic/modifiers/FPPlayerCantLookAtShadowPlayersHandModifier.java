package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class FPPlayerCantLookAtShadowPlayersHandModifier extends AbstractModifier {
    public FPPlayerCantLookAtShadowPlayersHandModifier(PhysicalCard source) {
        super(source, "The Free Peoples player may not look at or reveal cards in any Shadow player's hand", null, ModifierEffect.LOOK_OR_REVEAL_MODIFIER);
    }

    @Override
    public boolean canLookOrRevealCardsInHand(LotroGame game, String revealingPlayerId, String actingPlayerId) {
        if (actingPlayerId.equals(game.getGameState().getCurrentPlayerId())
                && !revealingPlayerId.equals(game.getGameState().getCurrentPlayerId()))
            return false;
        return true;
    }
}
