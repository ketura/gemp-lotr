package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class RoamingRule {
    private ModifiersLogic _modifiersLogic;

    public RoamingRule(ModifiersLogic modifiersLogic) {
        _modifiersLogic = modifiersLogic;
    }

    public void applyRule() {
        Filter roamingFilter = Filters.and(CardType.MINION, new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return (modifiersQuerying.getMinionSiteNumber(gameState, physicalCard) > gameState.getCurrentSiteNumber());
            }
        });

        _modifiersLogic.addAlwaysOnModifier(
                new KeywordModifier(null, roamingFilter, Keyword.ROAMING));
    }
}
