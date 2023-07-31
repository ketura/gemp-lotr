package com.gempukku.lotro.processes.lotronly.assign;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.decisions.PlayerAssignMinionsDecision;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.gamestate.GameState;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.actions.lotronly.SystemQueueAction;
import com.gempukku.lotro.effects.AssignmentPhaseEffect;
import com.gempukku.lotro.effects.TriggeringResultEffect;
import com.gempukku.lotro.effects.UnrespondableEffect;
import com.gempukku.lotro.processes.GameProcess;
import com.gempukku.lotro.effects.results.FreePlayerStartsAssigningResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FreePeoplePlayerAssignsMinionsGameProcess implements GameProcess {
    private Set<LotroPhysicalCard> _leftoverMinions;
    private final GameProcess _followingAssignments;
    private DefaultGame _game;

    public FreePeoplePlayerAssignsMinionsGameProcess(GameProcess followingAssignments) {
        _followingAssignments = followingAssignments;
    }

    @Override
    public void process(DefaultGame game) {
        _game = game;
        final SystemQueueAction action = new SystemQueueAction();
        action.appendEffect(
                new TriggeringResultEffect(new FreePlayerStartsAssigningResult(), "Free people player starts assigning"));
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(final DefaultGame game) {
                        final GameState gameState = game.getGameState();

                        final Collection<LotroPhysicalCard> minions = Filters.filterActive(game, CardType.MINION, Filters.assignableToSkirmish(Side.FREE_PEOPLE, true, false));
                        if (minions.size() > 0) {
                            final Collection<LotroPhysicalCard> freePeopleTargets =
                                    Filters.filterActive(game,
                                            Filters.and(
                                                    Filters.or(
                                                            CardType.COMPANION, CardType.ALLY),
                                                    Filters.assignableToSkirmish(Side.FREE_PEOPLE, true, false)));


                            game.getUserFeedback().sendAwaitingDecision(gameState.getCurrentPlayerId(),
                                    new PlayerAssignMinionsDecision(1, "Assign minions to companions or allies at home", freePeopleTargets, minions) {
                                        @Override
                                        public void decisionMade(String result) throws DecisionResultInvalidException {
                                            Map<LotroPhysicalCard, Set<LotroPhysicalCard>> assignments = getAssignmentsBasedOnResponse(result);

                                            Set<LotroPhysicalCard> unassignedMinions = new HashSet<>(Filters.filterActive(game, CardType.MINION));
                                            // Validate minion count (Defender)
                                            for (LotroPhysicalCard freeCard : assignments.keySet()) {
                                                Set<LotroPhysicalCard> minionsAssigned = assignments.get(freeCard);
                                                if (minionsAssigned.size() + getMinionsAssignedBeforeCount(freeCard, gameState) > 1 + game.getModifiersQuerying().getKeywordCount(game, freeCard, Keyword.DEFENDER))
                                                    throw new DecisionResultInvalidException(GameUtils.getFullName(freeCard) + " can't have so many minions assigned");
                                                unassignedMinions.removeAll(minionsAssigned);
                                            }

                                            if (!game.getModifiersQuerying().isValidAssignments(game, Side.FREE_PEOPLE, assignments))
                                                throw new DecisionResultInvalidException("Assignments are not valid for the effects affecting the cards");

                                            _leftoverMinions = unassignedMinions;

                                            action.appendEffect(
                                                    new AssignmentPhaseEffect(gameState.getCurrentPlayerId(), assignments, "Free People player assignments"));
                                        }
                                    });
                        } else {
                            _leftoverMinions = new HashSet<>(Filters.filterActive(game, CardType.MINION));
                        }
                    }
                });
        game.getActionsEnvironment().addActionToStack(action);
    }

    private int getMinionsAssignedBeforeCount(LotroPhysicalCard freeCard, GameState gameState) {
        for (Assignment assignment : gameState.getAssignments()) {
            if (assignment.getFellowshipCharacter() == freeCard)
                return assignment.getShadowCharacters().size();
        }
        return 0;
    }

    @Override
    public GameProcess getNextProcess() {
        return _game.getFormat().getAdventure().getAfterFellowshipAssignmentGameProcess(_leftoverMinions, _followingAssignments);
    }
}
