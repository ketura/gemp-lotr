package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.Action;

public class PlayersCantUseCardPhaseSpecialAbilitiesModifier extends AbstractModifier {
    private final Phase _phase;
    private final Filter _sourceFilters;

    public PlayersCantUseCardPhaseSpecialAbilitiesModifier(LotroPhysicalCard source, Phase phase, Filterable... sourceFilters) {
        this(source, null, phase, sourceFilters);
    }

    public PlayersCantUseCardPhaseSpecialAbilitiesModifier(LotroPhysicalCard source, Condition condition, Phase phase, Filterable... sourceFilters) {
        super(source, "Can't use " + phase.getHumanReadable() + " special abilities", null, condition, ModifierEffect.ACTION_MODIFIER);
        _phase = phase;
        _sourceFilters = Filters.and(sourceFilters);
    }

    @Override
    public boolean canPlayAction(DefaultGame game, String performingPlayer, Action action) {
        if (action.getType() == Action.Type.SPECIAL_ABILITY
                && action.getActionTimeword() == _phase
                && _sourceFilters.accepts(game, action.getActionSource()))
            return false;
        return true;
    }
}
