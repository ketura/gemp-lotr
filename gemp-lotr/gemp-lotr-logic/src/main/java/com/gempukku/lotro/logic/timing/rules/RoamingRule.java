package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

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

        _modifiersLogic.addAlwaysOnModifier(
                new AbstractModifier(null, "Has Roaming keyword", roamingFilter, new ModifierEffect[]{ModifierEffect.KEYWORD_MODIFIER, ModifierEffect.TWILIGHT_COST_MODIFIER}) {
                    @Override
                    public int getKeywordCount(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, int result) {
                        if (keyword == Keyword.ROAMING)
                            return result + 1;
                        return result;
                    }

                    @Override
                    public boolean hasKeyword(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, boolean result) {
                        if (keyword == Keyword.ROAMING)
                            return true;
                        return result;
                    }

                    @Override
                    public int getTwilightCost(GameState gameState, ModifiersQuerying modifiersLogic, PhysicalCard physicalCard, int result) {
                        return result + Math.min(0, modifiersLogic.getRoamingPenalty(gameState, physicalCard));
                    }
                });
    }
}
