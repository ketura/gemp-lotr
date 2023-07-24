package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class CantReplaceSiteByFPPlayerModifier extends AbstractModifier {
    public CantReplaceSiteByFPPlayerModifier(LotroPhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Can't be replaced by Free Peoples player", affectFilter, condition, ModifierEffect.REPLACE_SITE_MODIFIER);
    }

    @Override
    public boolean isSiteReplaceable(DefaultGame game, String playerId) {
        if (playerId.equals(game.getGameState().getCurrentPlayerId()))
            return false;
        return true;
    }
}
