package com.gempukku.lotro.logic.timing.processes.turn.assign;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.actions.SystemQueueAction;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.PlayerAssignMinionsDecision;
import com.gempukku.lotro.logic.effects.AssignmentPhaseEffect;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ShadowPlayerAssignsHisMinionsGameProcess implements GameProcess {
    private PlayOrder _shadowOrder;
    private String _playerId;
    private GameProcess _followingGameProcess;

    public ShadowPlayerAssignsHisMinionsGameProcess(PlayOrder shadowOrder, String playerId, GameProcess followingGameProcess) {
        _shadowOrder = shadowOrder;
        _playerId = playerId;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process(final LotroGame game) {
        GameState gameState = game.getGameState();
        Filter minionFilter = Filters.and(CardType.MINION, Filters.owner(_playerId));

        final Collection<PhysicalCard> minions = Filters.filterActive(gameState, game.getModifiersQuerying(), minionFilter, Filters.assignableToSkirmish(Side.SHADOW, true, false));
        if (minions.size() > 0) {
            final Collection<PhysicalCard> freePeopleTargets =
                    Filters.filterActive(gameState, game.getModifiersQuerying(),
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

                            if (!game.getModifiersQuerying().isValidAssignments(game.getGameState(), Side.SHADOW, assignments))
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
            return new ShadowPlayerAssignsHisMinionsGameProcess(_shadowOrder, nextPlayerId, _followingGameProcess);
        else
            return _followingGameProcess;
    }
}
