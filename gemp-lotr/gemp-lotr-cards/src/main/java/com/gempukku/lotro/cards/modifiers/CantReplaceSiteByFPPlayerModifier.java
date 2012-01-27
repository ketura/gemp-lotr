package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CantReplaceSiteByFPPlayerModifier extends AbstractModifier {
    public CantReplaceSiteByFPPlayerModifier(PhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Can't be replaced by Free Peoples player", affectFilter, condition, ModifierEffect.REPLACE_SITE_MODIFIER);
    }

    @Override
    public boolean isSiteReplaceable(GameState gameState, ModifiersQuerying modifiersQuerying, String playerId) {
        if (playerId.equals(gameState.getCurrentPlayerId()))
            return false;
        return true;
    }
}
