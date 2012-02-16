package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CantPlaySiteByFPPlayerModifier extends AbstractModifier {
    public CantPlaySiteByFPPlayerModifier(PhysicalCard source, Condition condition) {
        super(source, "Can't be replaced by Free Peoples player", null, condition, ModifierEffect.PLAY_SITE_MODIFIER);
    }

    @Override
    public boolean canPlaySite(GameState gameState, ModifiersQuerying modifiersQuerying, String playerId) {
        if (playerId.equals(gameState.getCurrentPlayerId()))
            return false;
        return true;
    }
}