package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.*;

import java.util.LinkedList;
import java.util.List;

public class RoamingRule {
    private ModifiersLogic _modifiersLogic;

    public RoamingRule(ModifiersLogic modifiersLogic) {
        _modifiersLogic = modifiersLogic;
    }

    public void applyRule() {
        Filter roamingFilter = Filters.and(Filters.type(CardType.MINION), new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return (physicalCard.getBlueprint().getSiteNumber() > gameState.getCurrentSiteNumber());
            }
        });

        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(new KeywordModifier(null, null, Keyword.ROAMING));
        modifiers.add(new TwilightCostModifier(null, null, 2));

        _modifiersLogic.addAlwaysOnModifier(
                new CompositeModifier(null, roamingFilter, modifiers));
    }
}
