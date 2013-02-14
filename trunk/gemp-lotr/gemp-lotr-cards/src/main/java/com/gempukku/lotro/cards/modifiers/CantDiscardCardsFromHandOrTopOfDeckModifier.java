package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CantDiscardCardsFromHandOrTopOfDeckModifier extends AbstractModifier {
    private Filter _discardSourceAffected;
    private String _playerId;

    public CantDiscardCardsFromHandOrTopOfDeckModifier(PhysicalCard source, Condition condition, String playerId, Filterable... discardSourceAffected) {
        super(source, null, null, condition, ModifierEffect.DISCARD_NOT_FROM_PLAY);
        _playerId = playerId;
        _discardSourceAffected = Filters.and(discardSourceAffected);
    }

    @Override
    public boolean canDiscardCardsFromHand(GameState gameState, ModifiersQuerying modifiersQuerying, String playerId, PhysicalCard source) {
        if (playerId.equals(_playerId))
            if (source == null || _discardSourceAffected.accepts(gameState, modifiersQuerying, source))
                return false;
        return true;
    }

    @Override
    public boolean canDiscardCardsFromTopOfDeck(GameState gameState, ModifiersQuerying modifiersQuerying, String playerId, PhysicalCard source) {
        if (playerId.equals(_playerId))
            if (source == null || _discardSourceAffected.accepts(gameState, modifiersQuerying, source))
                return false;
        return true;
    }
}
