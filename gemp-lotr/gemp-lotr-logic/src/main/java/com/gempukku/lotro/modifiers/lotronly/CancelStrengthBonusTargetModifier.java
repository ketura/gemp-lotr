package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.AbstractModifier;
import com.gempukku.lotro.modifiers.ModifierEffect;
import com.gempukku.lotro.modifiers.condition.Condition;

public class CancelStrengthBonusTargetModifier extends AbstractModifier {
    private final Filter _sourceFilter;

    public CancelStrengthBonusTargetModifier(LotroPhysicalCard source, Filterable affectFilter, Filterable sourceFilter) {
        this(source, null, affectFilter, sourceFilter);
    }

    public CancelStrengthBonusTargetModifier(LotroPhysicalCard source, Condition condition, Filterable affectFilter, Filterable sourceFilter) {
        super(source, "Has some strength bonuses cancelled", affectFilter, condition, ModifierEffect.STRENGTH_BONUS_TARGET_MODIFIER);
        _sourceFilter = Filters.and(sourceFilter);
    }

    @Override
    public boolean appliesStrengthBonusModifier(DefaultGame game, LotroPhysicalCard modifierSource, LotroPhysicalCard modifierTaget) {
        if (_sourceFilter == null || (modifierSource != null && _sourceFilter.accepts(game, modifierSource)))
            return false;
        return true;
    }
}
