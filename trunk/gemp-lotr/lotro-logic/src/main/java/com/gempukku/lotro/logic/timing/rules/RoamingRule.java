package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class RoamingRule {
    private ModifiersLogic _modifiersLogic;

    public RoamingRule(ModifiersLogic modifiersLogic) {
        _modifiersLogic = modifiersLogic;
    }

    public void applyRule() {
        _modifiersLogic.addAlwaysOnModifier(
                new AbstractModifier(null, "Roaming - +2 Twilight cost", Filters.and(Filters.type(CardType.MINION), new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        return (physicalCard.getBlueprint().getSiteNumber() > gameState.getCurrentSiteNumber());
                    }
                })) {
                    @Override
                    public int getTwilightCost(GameState gameState, ModifiersQuerying modifiersLogic, PhysicalCard physicalCard, int result) {
                        return 2 + result;
                    }
                }
        );
    }
}
