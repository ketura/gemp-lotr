package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;

public class CantReplaceSiteModifier extends AbstractModifier {
    public CantReplaceSiteModifier(LotroPhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Can't be replaced", affectFilter, condition, ModifierEffect.REPLACE_SITE_MODIFIER);
    }

    @Override
    public boolean isSiteReplaceable(DefaultGame game, String playerId) {
        return false;
    }
}
