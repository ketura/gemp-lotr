package com.gempukku.lotro.game.effects.choose;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.GameUtils;
import com.gempukku.lotro.game.effects.AssignmentEffect;
import com.gempukku.lotro.game.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.game.actions.SubAction;
import com.gempukku.lotro.game.actions.Action;

public class ChooseAndAssignMinionToCompanionEffect extends ChooseActiveCardEffect {
    private final Action _action;
    private final String _playerId;
    private final PhysicalCard _companion;

    public ChooseAndAssignMinionToCompanionEffect(Action action, String playerId, PhysicalCard companion, Filterable... filters) {
        super(action.getActionSource(), playerId, "Choose minion to assign " + GameUtils.getCardLink(companion) + " to", filters);
        _action = action;
        _playerId = playerId;
        _companion = companion;
    }

    @Override
    protected Filter getExtraFilterForPlaying(final DefaultGame game) {
        final Side side = game.getGameState().getCurrentPlayerId().equals(_playerId) ? Side.FREE_PEOPLE : Side.SHADOW;
        return Filters.assignableToSkirmishAgainst(side, _companion, false, false);
    }

    @Override
    protected void cardSelected(DefaultGame game, PhysicalCard card) {
        SubAction subAction = new SubAction(_action);
        subAction.appendEffect(
                new AssignmentEffect(_playerId, _companion, card) {
                    @Override
                    protected void assignmentMadeCallback(PhysicalCard fpChar, PhysicalCard minion) {
                        ChooseAndAssignMinionToCompanionEffect.this.assignmentMadeCallback(fpChar, minion);
                    }
                });
        game.getActionsEnvironment().addActionToStack(subAction);
    }

    protected void assignmentMadeCallback(PhysicalCard fpChar, PhysicalCard minion) {

    }
}
