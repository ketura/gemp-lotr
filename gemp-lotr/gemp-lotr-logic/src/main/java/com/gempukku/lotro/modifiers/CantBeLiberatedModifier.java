package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class CantBeLiberatedModifier extends AbstractModifier {
    private Side _side;

    public CantBeLiberatedModifier(LotroPhysicalCard source, Filterable affectFilter) {
        super(source, "Can't be liberated", affectFilter, ModifierEffect.LIBERATION_MODIFIER);
    }

    public CantBeLiberatedModifier(LotroPhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Can't be liberated", affectFilter, condition, ModifierEffect.LIBERATION_MODIFIER);
    }

    public CantBeLiberatedModifier(LotroPhysicalCard source, Side side, Condition condition, Filterable affectFilter) {
        super(source, "Can't be liberated", affectFilter, condition, ModifierEffect.LIBERATION_MODIFIER);
        _side = side;
    }

    @Override
    public boolean canBeLiberated(DefaultGame game, String performingPlayer, LotroPhysicalCard card, LotroPhysicalCard source) {
        return !(_side == null ||
                (_side == Side.SHADOW && !performingPlayer.equals(game.getGameState().getCurrentPlayerId())) ||
                (_side == Side.FREE_PEOPLE && performingPlayer.equals(game.getGameState().getCurrentPlayerId())));
    }
}
