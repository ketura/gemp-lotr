package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;

public class CantDiscardFromPlayByPlayerModifier extends AbstractModifier {
    private final String _notPlayer;

    public CantDiscardFromPlayByPlayerModifier(LotroPhysicalCard source, String text, Filterable affectFilter, String notPlayer) {
        super(source, text, affectFilter, ModifierEffect.DISCARD_FROM_PLAY_MODIFIER);
        _notPlayer = notPlayer;
    }

    public CantDiscardFromPlayByPlayerModifier(LotroPhysicalCard source, String text, Condition condition, Filterable affectFilter, String notPlayer) {
        super(source, text, affectFilter, condition, ModifierEffect.DISCARD_FROM_PLAY_MODIFIER);
        _notPlayer = notPlayer;
    }

    @Override
    public boolean canBeDiscardedFromPlay(DefaultGame game, String performingPlayer, LotroPhysicalCard card, LotroPhysicalCard source) {
        if (!_notPlayer.equals(performingPlayer))
            return false;
        return true;
    }
}
