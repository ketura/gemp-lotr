package com.gempukku.lotro.effects.choose;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.effects.AssignmentEffect;
import com.gempukku.lotro.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.actions.lotronly.SubAction;
import com.gempukku.lotro.actions.Action;

public class ChooseAndAssignMinionToCompanionEffect extends ChooseActiveCardEffect {
    private final Action _action;
    private final String _playerId;
    private final LotroPhysicalCard _companion;

    public ChooseAndAssignMinionToCompanionEffect(Action action, String playerId, LotroPhysicalCard companion, Filterable... filters) {
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
    protected void cardSelected(DefaultGame game, LotroPhysicalCard card) {
        SubAction subAction = new SubAction(_action);
        subAction.appendEffect(
                new AssignmentEffect(_playerId, _companion, card) {
                    @Override
                    protected void assignmentMadeCallback(LotroPhysicalCard fpChar, LotroPhysicalCard minion) {
                        ChooseAndAssignMinionToCompanionEffect.this.assignmentMadeCallback(fpChar, minion);
                    }
                });
        game.getActionsEnvironment().addActionToStack(subAction);
    }

    protected void assignmentMadeCallback(LotroPhysicalCard fpChar, LotroPhysicalCard minion) {

    }
}
