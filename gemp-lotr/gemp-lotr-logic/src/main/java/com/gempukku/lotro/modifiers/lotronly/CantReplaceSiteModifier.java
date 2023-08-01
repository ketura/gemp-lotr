package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.AbstractModifier;
import com.gempukku.lotro.modifiers.condition.Condition;
import com.gempukku.lotro.modifiers.ModifierEffect;

public class CantReplaceSiteModifier extends AbstractModifier {
    public CantReplaceSiteModifier(LotroPhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Can't be replaced", affectFilter, condition, ModifierEffect.REPLACE_SITE_MODIFIER);
    }

    @Override
    public boolean isSiteReplaceable(DefaultGame game, String playerId) {
        return false;
    }
}
