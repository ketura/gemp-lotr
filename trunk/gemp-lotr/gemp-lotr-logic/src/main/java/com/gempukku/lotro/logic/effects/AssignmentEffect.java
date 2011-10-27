package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.AssignmentResult;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AssignmentEffect extends AbstractEffect {
    private String _playerId;
    private PhysicalCard _fpChar;
    private PhysicalCard _minion;

    public AssignmentEffect(String playerId, PhysicalCard fpChar, PhysicalCard minion) {
        _playerId = playerId;
        _fpChar = fpChar;
        _minion = minion;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return "Assign " + GameUtils.getCardLink(_minion) + " to skirmish " + GameUtils.getCardLink(_fpChar);
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        Side side = _playerId.equals(game.getGameState().getCurrentPlayerId()) ? Side.FREE_PEOPLE : Side.SHADOW;
        return Filters.canBeAssignedToSkirmishByEffect(side).accepts(game.getGameState(), game.getModifiersQuerying(), _fpChar)
                && Filters.canBeAssignedToSkirmishByEffect(side).accepts(game.getGameState(), game.getModifiersQuerying(), _minion)
                && game.getModifiersQuerying().isValidAssignments(game.getGameState(), side, Collections.singletonMap(_fpChar, Collections.singletonList(_minion)));
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            game.getGameState().sendMessage(_playerId + " assigns " + GameUtils.getCardLink(_minion) + " to skirmish " + GameUtils.getCardLink(_fpChar));
            game.getGameState().assignToSkirmishes(_fpChar, Collections.singletonList(_minion));

            final Map<PhysicalCard, List<PhysicalCard>> assignments = Collections.singletonMap(_fpChar, Collections.singletonList(_minion));
            return new FullEffectResult(Collections.singleton(new AssignmentResult(_playerId, assignments)), true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}
