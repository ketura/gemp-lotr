package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;

public class TwilightCostModifier extends AbstractModifier {
    private int _twilightCostModifier;

    public TwilightCostModifier(PhysicalCard source, Filter affectFilter, int twilightCostModifier) {
        super(source, "Twilight cost " + ((twilightCostModifier < 0) ? twilightCostModifier : ("+" + twilightCostModifier)), affectFilter);
        _twilightCostModifier = twilightCostModifier;
    }

    @Override
    public int getTwilightCost(GameState gameState, ModifiersQuerying modifiersLogic, PhysicalCard physicalCard, int result) {
        return result + _twilightCostModifier;
    }
}
