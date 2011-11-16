package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.*;

public class RoamingRule {
    private ModifiersLogic _modifiersLogic;

    public RoamingRule(ModifiersLogic modifiersLogic) {
        _modifiersLogic = modifiersLogic;
    }

    public void applyRule() {
        Filter roamingFilter = Filters.and(Filters.type(CardType.MINION), new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return (modifiersQuerying.getMinionSiteNumber(gameState, physicalCard) > gameState.getCurrentSiteNumber());
            }
        });

        _modifiersLogic.addAlwaysOnModifier(
                new KeywordModifier(null, roamingFilter, Keyword.ROAMING));

        _modifiersLogic.addAlwaysOnModifier(
                new AbstractModifier(null, null, Filters.and(CardType.MINION, Keyword.ROAMING), ModifierEffect.TWILIGHT_COST_MODIFIER) {
                    @Override
                    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, boolean ignoreRoamingPenalty) {
                        if (ignoreRoamingPenalty)
                            return 0;
                        return modifiersQuerying.getRoamingPenalty(gameState, physicalCard);
                    }

                    @Override
                    public String getText(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
                        final int value = modifiersQuerying.getRoamingPenalty(gameState, self);
                        if (value >= 0)
                            return "Twilight cost +" + value;
                        else
                            return "Twilight cost " + value;
                    }
                });
    }
}
