package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class CantReplaceSiteByFPPlayerModifier extends AbstractModifier {
    public CantReplaceSiteByFPPlayerModifier(PhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Can't be replaced by Free Peoples player", affectFilter, condition, ModifierEffect.REPLACE_SITE_MODIFIER);
    }

    @Override
    public boolean isSiteReplaceable(LotroGame game, String playerId) {
        if (playerId.equals(game.getGameState().getCurrentPlayerId()))
            return false;
        return true;
    }
}
