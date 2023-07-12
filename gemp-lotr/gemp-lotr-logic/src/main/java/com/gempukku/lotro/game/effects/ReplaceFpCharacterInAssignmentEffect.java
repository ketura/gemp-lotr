package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.state.lotronly.Assignment;
import com.gempukku.lotro.game.timing.PlayConditions;

public class ReplaceFpCharacterInAssignmentEffect extends AbstractEffect {
    private final PhysicalCard _replacedBy;
    private final PhysicalCard _replacing;

    public ReplaceFpCharacterInAssignmentEffect(PhysicalCard replacedBy, PhysicalCard replacing) {
        _replacedBy = replacedBy;
        _replacing = replacing;
    }

    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return PlayConditions.isActive(game, _replacedBy) && PlayConditions.isActive(game, _replacing, Filters.assignedToSkirmish, Filters.not(Filters.inSkirmish));
    }

    private Assignment getAssignment(DefaultGame game, PhysicalCard fpChar) {
        for (Assignment assignment : game.getGameState().getAssignments()) {
            if (assignment.getFellowshipCharacter() == fpChar)
                return assignment;
        }
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        final Assignment assignment = getAssignment(game, _replacing);
        if (isPlayableInFull(game) && assignment != null) {
            game.getGameState().removeAssignment(assignment);
            game.getGameState().assignToSkirmishes(_replacedBy, assignment.getShadowCharacters());
            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }
}
