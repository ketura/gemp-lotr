package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.game.timing.results.AssignAgainstResult;
import com.gempukku.lotro.game.timing.results.AssignedToSkirmishResult;

import java.util.Collections;

public class AssignmentEffect extends AbstractEffect {
    private final String _playerId;
    private boolean _ignoreSingleMinionRestriction;
    private final boolean _skipAllyLocationCheck;
    private final LotroPhysicalCard _fpChar;
    private final LotroPhysicalCard _minion;

    public AssignmentEffect(String playerId, LotroPhysicalCard fpChar, LotroPhysicalCard minion) {
        this(playerId, fpChar, minion, false);
    }

    public AssignmentEffect(String playerId, LotroPhysicalCard fpChar, LotroPhysicalCard minion, boolean skipAllyLocationCheck) {
        _playerId = playerId;
        _fpChar = fpChar;
        _minion = minion;
        _skipAllyLocationCheck = skipAllyLocationCheck;
    }

    public void setIgnoreSingleMinionRestriction(boolean ignoreSingleMinionRestriction) {
        _ignoreSingleMinionRestriction = ignoreSingleMinionRestriction;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Assign " + GameUtils.getCardLink(_minion) + " to skirmish " + GameUtils.getCardLink(_fpChar);
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        Side side = _playerId.equals(game.getGameState().getCurrentPlayerId()) ? Side.FREE_PEOPLE : Side.SHADOW;
        return Filters.assignableToSkirmishAgainst(side, _fpChar, _ignoreSingleMinionRestriction, _skipAllyLocationCheck).accepts(game, _minion);
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (isPlayableInFull(game)) {
            if (Filters.notAssignedToSkirmish.accepts(game, _fpChar))
                game.getActionsEnvironment().emitEffectResult(new AssignedToSkirmishResult(_fpChar, _playerId));
            if (Filters.notAssignedToSkirmish.accepts(game, _minion))
                game.getActionsEnvironment().emitEffectResult(new AssignedToSkirmishResult(_minion, _playerId));

            game.getGameState().sendMessage(_playerId + " assigns " + GameUtils.getCardLink(_minion) + " to skirmish " + GameUtils.getCardLink(_fpChar));
            game.getGameState().assignToSkirmishes(_fpChar, Collections.singleton(_minion));

            game.getActionsEnvironment().emitEffectResult(new AssignAgainstResult(_playerId, _fpChar, _minion));
            game.getActionsEnvironment().emitEffectResult(new AssignAgainstResult(_playerId, _minion, _fpChar));

            assignmentMadeCallback(_fpChar, _minion);

            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }

    protected void assignmentMadeCallback(LotroPhysicalCard fpChar, LotroPhysicalCard minion) {

    }
}
