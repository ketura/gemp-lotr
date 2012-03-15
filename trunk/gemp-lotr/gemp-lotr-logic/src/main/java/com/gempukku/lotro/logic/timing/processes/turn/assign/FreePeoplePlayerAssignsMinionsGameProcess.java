package com.gempukku.lotro.logic.timing.processes.turn.assign;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.Assignment;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SystemQueueAction;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.PlayerAssignMinionsDecision;
import com.gempukku.lotro.logic.effects.AssignmentPhaseEffect;
import com.gempukku.lotro.logic.effects.TriggeringResultEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.results.FreePlayerStartsAssigningResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FreePeoplePlayerAssignsMinionsGameProcess implements GameProcess {
    private Set<PhysicalCard> _leftoverMinions;
    private GameProcess _followingAssignments;

    public FreePeoplePlayerAssignsMinionsGameProcess(GameProcess followingAssignments) {
        _followingAssignments = followingAssignments;
    }

    @Override
    public void process(LotroGame game) {
        final SystemQueueAction action = new SystemQueueAction();
        action.appendEffect(
                new TriggeringResultEffect(new FreePlayerStartsAssigningResult(), "Free people player starts assigning"));
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(final LotroGame game) {
                        final GameState gameState = game.getGameState();

                        final Collection<PhysicalCard> minions = Filters.filterActive(gameState, game.getModifiersQuerying(), CardType.MINION, Filters.assignableToSkirmish(Side.FREE_PEOPLE, true, false));
                        if (minions.size() > 0) {
                            final Collection<PhysicalCard> freePeopleTargets =
                                    Filters.filterActive(gameState, game.getModifiersQuerying(),
                                            Filters.and(
                                                    Filters.or(
                                                            CardType.COMPANION, CardType.ALLY),
                                                    Filters.assignableToSkirmish(Side.FREE_PEOPLE, true, false)));


                            game.getUserFeedback().sendAwaitingDecision(gameState.getCurrentPlayerId(),
                                    new PlayerAssignMinionsDecision(1, "Assign minions to companions or allies at home", freePeopleTargets, minions) {
                                        @Override
                                        public void decisionMade(String result) throws DecisionResultInvalidException {
                                            Map<PhysicalCard, Set<PhysicalCard>> assignments = getAssignmentsBasedOnResponse(result);

                                            Set<PhysicalCard> unassignedMinions = new HashSet<PhysicalCard>(minions);
                                            // Validate minion count (Defender)
                                            for (PhysicalCard freeCard : assignments.keySet()) {
                                                Set<PhysicalCard> minionsAssigned = assignments.get(freeCard);
                                                if (minionsAssigned.size() + getMinionsAssignedBeforeCount(freeCard, gameState) > 1 + game.getModifiersQuerying().getKeywordCount(game.getGameState(), freeCard, Keyword.DEFENDER))
                                                    throw new DecisionResultInvalidException(freeCard.getBlueprint().getName() + " can't have so many minions assigned");
                                                unassignedMinions.removeAll(minionsAssigned);
                                            }

                                            if (!game.getModifiersQuerying().isValidAssignments(game.getGameState(), Side.FREE_PEOPLE, assignments))
                                                throw new DecisionResultInvalidException("Assignments are not valid for the effects affecting the cards");

                                            _leftoverMinions = unassignedMinions;

                                            action.appendEffect(
                                                    new AssignmentPhaseEffect(gameState.getCurrentPlayerId(), assignments, "Free People player assignments"));
                                        }
                                    });
                        } else {
                            _leftoverMinions = new HashSet<PhysicalCard>();
                        }
                    }
                });
        game.getActionsEnvironment().addActionToStack(action);
    }

    private int getMinionsAssignedBeforeCount(PhysicalCard freeCard, GameState gameState) {
        for (Assignment assignment : gameState.getAssignments()) {
            if (assignment.getFellowshipCharacter() == freeCard)
                return assignment.getShadowCharacters().size();
        }
        return 0;
    }

    @Override
    public GameProcess getNextProcess() {
        return new ShadowPlayersAssignTheirMinionsGameProcess(_followingAssignments, _leftoverMinions);
    }
}
