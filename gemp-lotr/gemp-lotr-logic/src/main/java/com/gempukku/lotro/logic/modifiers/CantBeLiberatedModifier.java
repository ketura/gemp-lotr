package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class CantBeLiberatedModifier extends AbstractModifier {
    private Side _side;

    public CantBeLiberatedModifier(PhysicalCard source, Filterable affectFilter) {
        super(source, "Can't be liberated", affectFilter, ModifierEffect.LIBERATION_MODIFIER);
    }

    public CantBeLiberatedModifier(PhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Can't be liberated", affectFilter, condition, ModifierEffect.LIBERATION_MODIFIER);
    }

    public CantBeLiberatedModifier(PhysicalCard source, Side side, Condition condition, Filterable affectFilter) {
        super(source, "Can't be liberated", affectFilter, condition, ModifierEffect.LIBERATION_MODIFIER);
        _side = side;
    }

    @Override
    public boolean canBeLiberated(LotroGame game, String performingPlayer, PhysicalCard card, PhysicalCard source) {
        return !(_side == null ||
                (_side == Side.SHADOW && !performingPlayer.equals(game.getGameState().getCurrentPlayerId())) ||
                (_side == Side.FREE_PEOPLE && performingPlayer.equals(game.getGameState().getCurrentPlayerId())));
    }
}
