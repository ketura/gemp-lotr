package com.gempukku.lotro.effects;

import com.gempukku.lotro.actions.AbstractActionProxy;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.lotronly.RequiredTriggerAction;
import com.gempukku.lotro.game.TriggerConditions;

import java.util.Collections;
import java.util.List;

public class PreventAllWoundsActionProxy extends AbstractActionProxy {
    private final LotroPhysicalCard _source;
    private final Filter _filters;

    public PreventAllWoundsActionProxy(LotroPhysicalCard source, Filterable... filters) {
        _source = source;
        _filters = Filters.and(filters);
    }

    @Override
    public List<? extends RequiredTriggerAction> getRequiredBeforeTriggers(DefaultGame game, Effect effect) {
        if (TriggerConditions.isGettingWounded(effect, game, _filters)) {
            WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            RequiredTriggerAction action = new RequiredTriggerAction(_source);
            action.appendEffect(
                    new PreventCardEffect(woundEffect, _filters));
            return Collections.singletonList(action);
        }
        return null;
    }
}
