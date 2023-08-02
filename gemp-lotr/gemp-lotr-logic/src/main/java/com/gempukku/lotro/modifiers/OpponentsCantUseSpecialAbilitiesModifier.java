package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.Action;
import com.gempukku.lotro.modifiers.condition.Condition;

public class OpponentsCantUseSpecialAbilitiesModifier extends AbstractModifier {
    private final String _playerId;

    public OpponentsCantUseSpecialAbilitiesModifier(LotroPhysicalCard source, String playerId) {
        this(source, null, playerId);
    }

    public OpponentsCantUseSpecialAbilitiesModifier(LotroPhysicalCard source, Condition condition, String playerId) {
        super(source, null, null, condition, ModifierEffect.ACTION_MODIFIER);
        _playerId = playerId;
    }

    @Override
    public boolean canPlayAction(DefaultGame game, String performingPlayer, Action action) {
        if (action.getType() == Action.Type.SPECIAL_ABILITY
                && !performingPlayer.equals(_playerId))
            return false;
        return true;
    }
}
