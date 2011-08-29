package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class AllyOnCurrentSiteModifier extends AbstractModifier {
    public AllyOnCurrentSiteModifier(PhysicalCard source, Filter affectFilter) {
        super(source, "Can participate in archery and skirmishes", affectFilter);
    }

    @Override
    public boolean isAllyOnCurrentSite(GameState gameState, ModifiersQuerying modifiersLogic, PhysicalCard card, boolean allyOnCurrentSite) {
        return true;
    }
}
