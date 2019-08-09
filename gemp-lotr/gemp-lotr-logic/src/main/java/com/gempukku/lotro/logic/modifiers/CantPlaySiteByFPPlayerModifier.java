package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class CantPlaySiteByFPPlayerModifier extends AbstractModifier {
    public CantPlaySiteByFPPlayerModifier(PhysicalCard source, Condition condition) {
        super(source, "Can't be replaced by Free Peoples player", null, condition, ModifierEffect.PLAY_SITE_MODIFIER);
    }

    @Override
    public boolean canPlaySite(LotroGame game, String playerId) {
        if (playerId.equals(game.getGameState().getCurrentPlayerId()))
            return false;
        return true;
    }
}