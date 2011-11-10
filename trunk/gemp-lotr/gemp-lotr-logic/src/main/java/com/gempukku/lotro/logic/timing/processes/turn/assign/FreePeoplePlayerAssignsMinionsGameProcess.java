package com.gempukku.lotro.logic.timing.processes.turn.assign;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SystemQueueAction;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.PlayerAssignMinionsDecision;
import com.gempukku.lotro.logic.effects.AssignmentPhaseEffect;
import com.gempukku.lotro.logic.effects.TriggeringResultEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.results.FreePlayerStartsAssigningResult;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FreePeoplePlayerAssignsMinionsGameProcess implements GameProcess {
    private GameProcess _followingGameProcess;

    public FreePeoplePlayerAssignsMinionsGameProcess(LotroGame game, GameProcess followingGameProcess) {
        _followingGameProcess = followingGameProcess;
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

                        Filter minionFilter = Filters.type(CardType.MINION);
                        if (gameState.isFierceSkirmishes())
                            minionFilter = Filters.and(
                                    Filters.keyword(Keyword.FIERCE),
                                    minionFilter);

                        final Collection<PhysicalCard> minions = Filters.filterActive(gameState, game.getModifiersQuerying(), minionFilter, Filters.canBeAssignedToSkirmish(Side.FREE_PEOPLE));
                        if (minions.size() > 0) {
                            final Collection<PhysicalCard> freePeopleTargets = Filters.filterActive(gameState, game.getModifiersQuerying(),
                                    Filters.or(
                                            Filters.and(
                                                    Filters.type(CardType.COMPANION),
                                                    Filters.or(
                                                            Filters.not(Keyword.UNHASTY),
                                                            new Filter() {
                                                                @Override
                                                                public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                                                    return modifiersQuerying.isAllowedToParticipateInSkirmishes(gameState, Side.FREE_PEOPLE, physicalCard);
                                                                }
                                                            }
                                                    )
                                            ),
                                            Filters.and(
                                                    Filters.type(CardType.ALLY),
                                                    Filters.or(
                                                            Filters.and(
                                                                    Filters.allyAtHome,
                                                                    new Filter() {
                                                                        @Override
                                                                        public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                                                            return !modifiersQuerying.isAllyPreventedFromParticipatingInSkirmishes(gameState, Side.FREE_PEOPLE, physicalCard);
                                                                        }
                                                                    }),
                                                            new Filter() {
                                                                @Override
                                                                public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                                                    return modifiersQuerying.isAllowedToParticipateInSkirmishes(gameState, Side.FREE_PEOPLE, physicalCard);
                                                                }
                                                            }
                                                    )
                                            )
                                    ),
                                    Filters.canBeAssignedToSkirmish(Side.FREE_PEOPLE));

                            game.getUserFeedback().sendAwaitingDecision(gameState.getCurrentPlayerId(),
                                    new PlayerAssignMinionsDecision(1, "Assign minions to companions or allies at home", freePeopleTargets, minions) {
                                        @Override
                                        public void decisionMade(String result) throws DecisionResultInvalidException {
                                            Map<PhysicalCard, List<PhysicalCard>> assignments = getAssignmentsBasedOnResponse(result);

                                            List<PhysicalCard> unassignedMinions = new LinkedList<PhysicalCard>(minions);
                                            // Validate minion count (Defender)
                                            for (PhysicalCard freeCard : assignments.keySet()) {
                                                List<PhysicalCard> minionsAssigned = assignments.get(freeCard);
                                                if (minionsAssigned.size() > 1 + game.getModifiersQuerying().getKeywordCount(game.getGameState(), freeCard, Keyword.DEFENDER))
                                                    throw new DecisionResultInvalidException(freeCard.getBlueprint().getName() + " can't have so many minions assigned");
                                                unassignedMinions.removeAll(minionsAssigned);
                                            }

                                            if (!game.getModifiersQuerying().isValidAssignments(game.getGameState(), Side.FREE_PEOPLE, assignments))
                                                throw new DecisionResultInvalidException("Assignments are not valid for the effects affecting the cards");

                                            action.appendEffect(
                                                    new AssignmentPhaseEffect(gameState.getCurrentPlayerId(), assignments, "Free People player assignments"));
                                        }
                                    });
                        }

                    }
                });
        game.getActionsEnvironment().addActionToStack(action);
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
