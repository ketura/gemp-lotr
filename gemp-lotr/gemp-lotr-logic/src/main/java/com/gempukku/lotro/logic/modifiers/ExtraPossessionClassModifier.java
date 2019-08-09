package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class ExtraPossessionClassModifier extends AbstractModifier {
    private Filterable affectFilter;
    private Filterable attachedToFilter;

    public ExtraPossessionClassModifier(PhysicalCard source, Filterable affectFilter) {
        this(source, null, affectFilter, Filters.any);
    }

    public ExtraPossessionClassModifier(PhysicalCard source, Condition condition, Filterable affectFilter, Filterable attachedToFilter) {
        super(source, "Can be worn in addition to another item of the same class", affectFilter, condition, ModifierEffect.EXTRA_POSSESSION_CLASS_MODIFIER);
        this.affectFilter = affectFilter;
        this.attachedToFilter = attachedToFilter;
    }

    @Override
    public boolean isExtraPossessionClass(LotroGame game, PhysicalCard physicalCard, PhysicalCard attachedTo) {
        return Filters.and(attachedToFilter).accepts(game, attachedTo);
    }
}
