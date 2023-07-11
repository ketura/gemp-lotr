package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class CantPlaySiteByFPPlayerModifier extends AbstractModifier {
    public CantPlaySiteByFPPlayerModifier(PhysicalCard source, Condition condition) {
        super(source, "Can't be replaced by Free Peoples player", null, condition, ModifierEffect.PLAY_SITE_MODIFIER);
    }

    @Override
    public boolean canPlaySite(DefaultGame game, String playerId) {
        if (playerId.equals(game.getGameState().getCurrentPlayerId()))
            return false;
        return true;
    }
}