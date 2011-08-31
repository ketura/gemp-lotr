package com.gempukku.lotro.logic.timing.processes.turn.skirmish;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.SkirmishGameProcess;

import java.util.LinkedList;
import java.util.List;

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
        if (_game.getModifiersQuerying().shouldSkipPhase(_game.getGameState(), Phase.SKIRMISH)) {
            _nextProcess = _followingGameProcess;
        } else {
            final GameState gameState = _game.getGameState();
            List<Skirmish> assignments = gameState.getAssignments();

            if (assignments.size() > 0) {
                List<PhysicalCard> fps = new LinkedList<PhysicalCard>();
                for (Skirmish assignment : assignments)
                    fps.add(assignment.getFellowshipCharacter());

                _game.getUserFeedback().sendAwaitingDecision(gameState.getCurrentPlayerId(),
                        new CardsSelectionDecision(1, "Choose next skirmish to resolve", fps, 1, 1) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                PhysicalCard fp = getSelectedCardsByResponse(result).get(0);
                                gameState.startSkirmish(fp);

                                _nextProcess = new SkirmishGameProcess(_game, new PlayoutSkirmishesGameProcess(_game, _followingGameProcess));
                            }
                        });
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
