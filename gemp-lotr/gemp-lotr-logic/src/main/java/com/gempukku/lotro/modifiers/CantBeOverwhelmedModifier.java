package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;

public class CantBeOverwhelmedModifier extends AbstractModifier {

    public CantBeOverwhelmedModifier(LotroPhysicalCard source, Filterable affectFilter) {
        this(source, affectFilter, null);
    }

    public CantBeOverwhelmedModifier(LotroPhysicalCard source, Filterable affectFilter, Condition condition) {
        super(source, "Cannot be overwhelmed", affectFilter, condition, ModifierEffect.OVERWHELM_MODIFIER);
    }

    @Override
    public int getOverwhelmMultiplier(DefaultGame game, LotroPhysicalCard physicalCard) {
        return 10000;
    }
}
