package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

public class OpponentsCantUseSpecialAbilitiesModifier extends AbstractModifier {
    private String _playerId;

    public OpponentsCantUseSpecialAbilitiesModifier(PhysicalCard source, String playerId) {
        this(source, null, playerId);
    }

    public OpponentsCantUseSpecialAbilitiesModifier(PhysicalCard source, Condition condition, String playerId) {
        super(source, null, null, condition, ModifierEffect.ACTION_MODIFIER);
        _playerId = playerId;
    }

    @Override
    public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, String performingPlayer, Action action) {
        if (!performingPlayer.equals(_playerId) && action.getType() == Action.Type.SPECIAL_ABILITY)
            return false;
        return true;
    }
}
