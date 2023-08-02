package com.gempukku.lotro.processes.lotronly.assign;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.decisions.PlayerAssignMinionsDecision;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.gamestate.GameState;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.PlayOrder;
import com.gempukku.lotro.actions.lotronly.SystemQueueAction;
import com.gempukku.lotro.effects.AssignmentPhaseEffect;
import com.gempukku.lotro.processes.GameProcess;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ShadowPlayerAssignsHisMinionsGameProcess implements GameProcess {
    private final PlayOrder _shadowOrder;
    private final String _playerId;
    private final Set<LotroPhysicalCard> _leftoverMinions;
    private final GameProcess _followingGameProcess;

    public ShadowPlayerAssignsHisMinionsGameProcess(PlayOrder shadowOrder, String playerId, Set<LotroPhysicalCard> leftoverMinions, GameProcess followingGameProcess) {
        _shadowOrder = shadowOrder;
        _playerId = playerId;
        _leftoverMinions = leftoverMinions;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process(final DefaultGame game) {
        GameState gameState = game.getGameState();
        Filter minionFilter = Filters.and(CardType.MINION, Filters.owner(_playerId), Filters.in(_leftoverMinions));

        final Collection<LotroPhysicalCard> minions = Filters.filterActive(game, minionFilter, Filters.assignableToSkirmish(Side.SHADOW, true, false));
        if (minions.size() > 0) {
            final Collection<LotroPhysicalCard> freePeopleTargets =
                    Filters.filterActive(game,
                            Filters.and(
                                    Filters.or(
                                            CardType.COMPANION, CardType.ALLY),
                                    Filters.assignableToSkirmish(Side.SHADOW, true, false)));

            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new PlayerAssignMinionsDecision(1, "Assign minions to companions or allies at home", freePeopleTargets, minions) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            Map<LotroPhysicalCard, Set<LotroPhysicalCard>> assignments = getAssignmentsBasedOnResponse(result);

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
