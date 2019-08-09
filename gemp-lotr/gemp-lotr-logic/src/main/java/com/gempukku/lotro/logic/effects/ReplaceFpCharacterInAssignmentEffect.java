package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.Assignment;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

public class ReplaceFpCharacterInAssignmentEffect extends AbstractEffect {
    private PhysicalCard _replacedBy;
    private PhysicalCard _replacing;

    public ReplaceFpCharacterInAssignmentEffect(PhysicalCard replacedBy, PhysicalCard replacing) {
        _replacedBy = replacedBy;
        _replacing = replacing;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return PlayConditions.isActive(game, _replacedBy) && PlayConditions.isActive(game, _replacing, Filters.assignedToSkirmish, Filters.not(Filters.inSkirmish));
    }

    private Assignment getAssignment(LotroGame game, PhysicalCard fpChar) {
        for (Assignment assignment : game.getGameState().getAssignments()) {
            if (assignment.getFellowshipCharacter() == fpChar)
                return assignment;
        }
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        final Assignment assignment = getAssignment(game, _replacing);
        if (isPlayableInFull(game) && assignment != null) {
            game.getGameState().removeAssignment(assignment);
            game.getGameState().assignToSkirmishes(_replacedBy, assignment.getShadowCharacters());
            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}
