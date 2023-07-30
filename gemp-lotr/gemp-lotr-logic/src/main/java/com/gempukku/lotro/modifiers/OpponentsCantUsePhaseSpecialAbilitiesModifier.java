package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.Action;

public class OpponentsCantUsePhaseSpecialAbilitiesModifier extends AbstractModifier {
    private final Phase _phase;
    private final String _playerId;

    public OpponentsCantUsePhaseSpecialAbilitiesModifier(LotroPhysicalCard source, Phase phase, String playerId) {
        this(source, null, phase, playerId);
    }

    public OpponentsCantUsePhaseSpecialAbilitiesModifier(LotroPhysicalCard source, Condition condition, Phase phase, String playerId) {
        super(source, null, null, condition, ModifierEffect.ACTION_MODIFIER);
        _phase = phase;
        _playerId = playerId;
    }

    @Override
    public boolean canPlayAction(DefaultGame game, String performingPlayer, Action action) {
        if (action.getType() == Action.Type.SPECIAL_ABILITY
                && !performingPlayer.equals(_playerId) && action.getActionTimeword() == _phase)
            return false;
        return true;
    }
}
