package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;

public class CantDiscardFromPlayByPlayerModifier extends AbstractModifier {
    private String _notPlayer;

    public CantDiscardFromPlayByPlayerModifier(PhysicalCard source, String text, Filterable affectFilter, String notPlayer) {
        super(source, text, affectFilter, ModifierEffect.DISCARD_FROM_PLAY_MODIFIER);
        _notPlayer = notPlayer;
    }

    public CantDiscardFromPlayByPlayerModifier(PhysicalCard source, String text, Condition condition, Filterable affectFilter, String notPlayer) {
        super(source, text, affectFilter, condition, ModifierEffect.DISCARD_FROM_PLAY_MODIFIER);
        _notPlayer = notPlayer;
    }

    @Override
    public boolean canBeDiscardedFromPlay(GameState gameState, ModifiersQuerying modifiersQuerying, String performingPlayer, PhysicalCard card, PhysicalCard source) {
        if (!_notPlayer.equals(performingPlayer))
            return false;
        return true;
    }
}
