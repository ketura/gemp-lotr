package com.gempukku.lotro.logic.timing.processes.turn.archery;

import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

import java.util.LinkedList;
import java.util.List;

public class FellowshipPlayerChoosesShadowPlayerToAssignDamageToGameProcess implements GameProcess {
    private int _woundsToAssign;
    private GameProcess _followingGameProcess;

    private GameProcess _nextProcess;

    public FellowshipPlayerChoosesShadowPlayerToAssignDamageToGameProcess(int woundsToAssign, GameProcess followingGameProcess) {
        _woundsToAssign = woundsToAssign;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process(LotroGame game) {
        if (_woundsToAssign > 0) {
            List<String> shadowPlayers = new LinkedList<String>();

            GameState gameState = game.getGameState();
            PlayOrder playOrder = gameState.getPlayerOrder().getClockwisePlayOrder(gameState.getCurrentPlayerId(), false);
            playOrder.getNextPlayer();

            String shadowPlayer;

            while ((shadowPlayer = playOrder.getNextPlayer()) != null)
                shadowPlayers.add(shadowPlayer);

            if (shadowPlayers.size() > 1) {
                game.getUserFeedback().sendAwaitingDecision(gameState.getCurrentPlayerId(),
                        new MultipleChoiceAwaitingDecision(1, "Choose shadow player to assign archery damage to", shadowPlayers.toArray(new String[shadowPlayers.size()])) {
                            @Override
                            protected void validDecisionMade(int index, String result) {
                                _nextProcess = new ShadowPlayerAssignsArcheryDamageGameProcess(result, _woundsToAssign, _followingGameProcess);
                            }
                        });
            } else {
                _nextProcess = new ShadowPlayerAssignsArcheryDamageGameProcess(shadowPlayers.get(0), _woundsToAssign, _followingGameProcess);
            }
        } else {
            _nextProcess = _followingGameProcess;
        }
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
