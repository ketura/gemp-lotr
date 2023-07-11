package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class CantBeOverwhelmedModifier extends AbstractModifier {

    public CantBeOverwhelmedModifier(PhysicalCard source, Filterable affectFilter) {
        this(source, affectFilter, null);
    }

    public CantBeOverwhelmedModifier(PhysicalCard source, Filterable affectFilter, Condition condition) {
        super(source, "Cannot be overwhelmed", affectFilter, condition, ModifierEffect.OVERWHELM_MODIFIER);
    }

    @Override
    public int getOverwhelmMultiplier(DefaultGame game, PhysicalCard physicalCard) {
        return 10000;
    }
}
