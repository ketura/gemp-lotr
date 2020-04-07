package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class CantReplaceSiteModifier extends AbstractModifier {
    public CantReplaceSiteModifier(PhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Can't be replaced", affectFilter, condition, ModifierEffect.REPLACE_SITE_MODIFIER);
    }

    @Override
    public boolean isSiteReplaceable(LotroGame game, String playerId) {
        return false;
    }
}
