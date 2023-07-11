package com.gempukku.lotro.game.timing.processes.turn.lotro.assign;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.PlayOrder;
import com.gempukku.lotro.game.actions.SystemQueueAction;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.game.decisions.PlayerAssignMinionsDecision;
import com.gempukku.lotro.game.effects.AssignmentPhaseEffect;
import com.gempukku.lotro.game.timing.processes.GameProcess;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ShadowPlayerAssignsHisMinionsGameProcess implements GameProcess {
    private final PlayOrder _shadowOrder;
    private final String _playerId;
    private final Set<PhysicalCard> _leftoverMinions;
    private final GameProcess _followingGameProcess;

    public ShadowPlayerAssignsHisMinionsGameProcess(PlayOrder shadowOrder, String playerId, Set<PhysicalCard> leftoverMinions, GameProcess followingGameProcess) {
        _shadowOrder = shadowOrder;
        _playerId = playerId;
        _leftoverMinions = leftoverMinions;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process(final DefaultGame game) {
        GameState gameState = game.getGameState();
        Filter minionFilter = Filters.and(CardType.MINION, Filters.owner(_playerId), Filters.in(_leftoverMinions));

        final Collection<PhysicalCard> minions = Filters.filterActive(game, minionFilter, Filters.assignableToSkirmish(Side.SHADOW, true, false));
        if (minions.size() > 0) {
            final Collection<PhysicalCard> freePeopleTargets =
                    Filters.filterActive(game,
                            Filters.and(
                                    Filters.or(
                                            CardType.COMPANION, CardType.ALLY),
                                    Filters.assignableToSkirmish(Side.SHADOW, true, false)));

            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new PlayerAssignMinionsDecision(1, "Assign minions to companions or allies at home", freePeopleTargets, minions) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            Map<PhysicalCard, Set<PhysicalCard>> assignments = getAssignmentsBasedOnResponse(result);

                            SystemQueueAction action = new SystemQueueAction();
                            action.appendEffect(
                                    new AssignmentPhaseEffect(_playerId, assignments, "Shadow player assignments"));

                            if (!game.getModifiersQuerying().isValidAssignments(game, Side.SHADOW, assignments))
                                throw new DecisionResultInvalidException("Assignments are not valid for the effects affecting the cards");

                            game.getActionsEnvironment().addActionToStack(action);
                        }
                    });
        }
    }

    @Override
    public GameProcess getNextProcess() {
        String nextPlayerId = _shadowOrder.getNextPlayer();
        if (nextPlayerId != null)
            return new ShadowPlayerAssignsHisMinionsGameProcess(_shadowOrder, nextPlayerId, _leftoverMinions, _followingGameProcess);
        else
            return _followingGameProcess;
    }
}
