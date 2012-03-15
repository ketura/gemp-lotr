package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CantReplaceSiteModifier extends AbstractModifier {
    public CantReplaceSiteModifier(PhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Can't be replaced", affectFilter, condition, ModifierEffect.REPLACE_SITE_MODIFIER);
    }

    @Override
    public boolean isSiteReplaceable(GameState gameState, ModifiersQuerying modifiersQuerying, String playerId) {
        return false;
    }
}
