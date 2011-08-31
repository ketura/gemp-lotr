package com.gempukku.lotro.logic.timing.processes.turn.general;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.decisions.ActionsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.GatherPlayableActionsVisitor;

import java.util.LinkedList;
import java.util.List;

public class PlayersPlayPhaseActionsInOrderGameProcess implements GameProcess {
    private LotroGame _game;
    private PlayOrder _playOrder;
    private int _consecutivePasses;
    private GameProcess _followingGameProcess;

    private GameProcess _nextProcess;

    public PlayersPlayPhaseActionsInOrderGameProcess(LotroGame game, PlayOrder playOrder, int consecutivePasses, GameProcess followingGameProcess) {
        _game = game;
        _playOrder = playOrder;
        _consecutivePasses = consecutivePasses;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process() {
        String playerId = _playOrder.getNextPlayer();
        GatherPlayableActionsVisitor visitor = new GatherPlayableActionsVisitor(_game, playerId);
        _game.getGameState().iterateActivableCards(playerId, visitor);

        List<? extends Action> actions = visitor.getActions();

        List<Action> playableActions = new LinkedList<Action>();
        for (Action action : actions)
            if (_game.getModifiersQuerying().canPlayAction(_game.getGameState(), action))
                playableActions.add(action);

        _game.getUserFeedback().sendAwaitingDecision(playerId,
                new ActionsSelectionDecision(1, "Choose action to play or press DONE", playableActions) {
                    @Override
                    public void decisionMade(String result) throws DecisionResultInvalidException {
                        Action action = getSelectedAction(result);
                        if (action != null) {
                            _nextProcess = new PlayersPlayPhaseActionsInOrderGameProcess(_game, _playOrder, 0, _followingGameProcess);
                            _game.getActionsEnvironment().addActionToStack(action);
                        } else {
                            _consecutivePasses++;
                            if (_consecutivePasses >= _playOrder.getPlayerCount())
                                _nextProcess = _followingGameProcess;
                            else
                                _nextProcess = new PlayersPlayPhaseActionsInOrderGameProcess(_game, _playOrder, _consecutivePasses, _followingGameProcess);
                        }
                    }
                });
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
