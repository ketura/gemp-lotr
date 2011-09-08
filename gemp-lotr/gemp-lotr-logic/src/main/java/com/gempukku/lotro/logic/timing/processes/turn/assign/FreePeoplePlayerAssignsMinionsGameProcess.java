package com.gempukku.lotro.logic.timing.processes.turn.assign;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.PlayerAssignMinionsDecision;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FreePeoplePlayerAssignsMinionsGameProcess implements GameProcess {
    private LotroGame _game;
    private GameProcess _followingGameProcess;

    public FreePeoplePlayerAssignsMinionsGameProcess(LotroGame game, GameProcess followingGameProcess) {
        _game = game;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process() {
        GameState gameState = _game.getGameState();

        Filter minionFilter = Filters.type(CardType.MINION);
        if (gameState.isFierceSkirmishes())
            minionFilter = Filters.and(
                    Filters.keyword(Keyword.FIERCE),
                    minionFilter);

        final List<PhysicalCard> minions = Filters.filterActive(gameState, _game.getModifiersQuerying(), minionFilter);
        if (minions.size() > 0) {
            final List<PhysicalCard> freePeopleTargets = Filters.filterActive(gameState, _game.getModifiersQuerying(),
                    Filters.or(
                            Filters.type(CardType.COMPANION),
                            new Filter() {
                                @Override
                                public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                    return modifiersQuerying.isAllyOnCurrentSite(gameState, physicalCard);
                                }
                            }));

            _game.getUserFeedback().sendAwaitingDecision(gameState.getCurrentPlayerId(),
                    new PlayerAssignMinionsDecision(1, "Assign minions to companions or allies at home", freePeopleTargets, minions) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            Map<PhysicalCard, List<PhysicalCard>> assignments = getAssignmentsBasedOnResponse(result);

                            List<PhysicalCard> unassignedMinions = new LinkedList<PhysicalCard>(minions);
                            // Validate minion count (Defender)
                            for (PhysicalCard freeCard : assignments.keySet()) {
                                List<PhysicalCard> minionsAssigned = assignments.get(freeCard);
                                if (minionsAssigned.size() > 1 + _game.getModifiersQuerying().getKeywordCount(_game.getGameState(), freeCard, Keyword.DEFENDER))
                                    throw new DecisionResultInvalidException(freeCard.getBlueprint().getName() + " can't have so many minions assigned");
                                unassignedMinions.removeAll(minionsAssigned);
                            }

                            if (!_game.getModifiersQuerying().isValidFreePlayerAssignments(_game.getGameState(), assignments))
                                throw new DecisionResultInvalidException("Assignments are not valid for the effects affecting the cards");

                            for (Map.Entry<PhysicalCard, List<PhysicalCard>> physicalCardListEntry : assignments.entrySet()) {
                                PhysicalCard fp = physicalCardListEntry.getKey();
                                List<PhysicalCard> minions = physicalCardListEntry.getValue();
                                if (minions != null && minions.size() > 0)
                                    _game.getGameState().assignToSkirmishes(fp, minions);
                            }
                        }
                    });
        }
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
