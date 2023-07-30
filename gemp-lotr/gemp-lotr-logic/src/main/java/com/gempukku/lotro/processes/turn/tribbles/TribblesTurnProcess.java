package com.gempukku.lotro.processes.turn.tribbles;

import com.gempukku.lotro.game.TribblesGame;
import com.gempukku.lotro.processes.DefaultGameProcess;
import com.gempukku.lotro.processes.GameProcess;

public class TribblesTurnProcess extends DefaultGameProcess<TribblesGame> {
    private GameProcess _followingGameProcess;
    @Override
    public void process(TribblesGame game) {
//        game.getGameState().sendMessage("DEBUG: Beginning TribblesTurnProcess");
        _followingGameProcess = new TribblesPlayerPlaysOrDraws(game.getGameState().getCurrentPlayerId(),
                new TribblesEndOfTurnGameProcess()
        );
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
