package com.gempukku.lotro.effects;

import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.gamestate.lotronly.Assignment;
import com.gempukku.lotro.game.PlayConditions;

public class ReplaceFpCharacterInAssignmentEffect extends AbstractEffect {
    private final LotroPhysicalCard _replacedBy;
    private final LotroPhysicalCard _replacing;

    public ReplaceFpCharacterInAssignmentEffect(LotroPhysicalCard replacedBy, LotroPhysicalCard replacing) {
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

    private Assignment getAssignment(DefaultGame game, LotroPhysicalCard fpChar) {
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
