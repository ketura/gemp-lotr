package com.gempukku.lotro.logic.timing.processes.turn.skirmish;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.Assignment;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SystemQueueAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.SkirmishGameProcess;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayoutSkirmishesGameProcess implements GameProcess {
    private LotroGame _game;
    private GameProcess _followingGameProcess;

    private GameProcess _nextProcess;

    public PlayoutSkirmishesGameProcess(LotroGame game, GameProcess followingGameProcess) {
        _game = game;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process() {
        if (_game.getModifiersQuerying().shouldSkipPhase(_game.getGameState(), Phase.SKIRMISH, null)) {
            _nextProcess = _followingGameProcess;
        } else {
            final GameState gameState = _game.getGameState();
            List<Assignment> assignments = gameState.getAssignments();

            if (assignments.size() > 0) {
                Set<PhysicalCard> fps = new HashSet<PhysicalCard>();
                for (Assignment assignment : assignments)
                    fps.add(assignment.getFellowshipCharacter());

                SystemQueueAction chooseNextSkirmishAction = new SystemQueueAction();
                chooseNextSkirmishAction.appendEffect(
                        new ChooseActiveCardEffect(null, gameState.getCurrentPlayerId(), "Choose next skirmish to resolve", Filters.in(fps)) {
                            @Override
                            protected void cardSelected(LotroGame game, PhysicalCard card) {
                                gameState.startSkirmish(card);

                                _nextProcess = new SkirmishGameProcess(_game, new PlayoutSkirmishesGameProcess(_game, _followingGameProcess));
                            }
                        });
                _game.getActionsEnvironment().addActionToStack(chooseNextSkirmishAction);
            } else {
                _nextProcess = _followingGameProcess;
            }
        }
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
