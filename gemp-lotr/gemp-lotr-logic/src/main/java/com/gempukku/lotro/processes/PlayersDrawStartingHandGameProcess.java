package com.gempukku.lotro.processes;

import com.gempukku.lotro.gamestate.GameState;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.processes.lotronly.MulliganProcess;
import com.gempukku.lotro.processes.turn.tribbles.TribblesStartOfTurnGameProcess;

public class PlayersDrawStartingHandGameProcess implements GameProcess {
    private final String _firstPlayer;
    private GameProcess _followingGameProcess;

    public PlayersDrawStartingHandGameProcess(String firstPlayer) {
        _firstPlayer = firstPlayer;
    }

    @Override
    public void process(DefaultGame game) {

        GameState gameState = game.getGameState();
        for (String player : gameState.getPlayerOrder().getAllPlayers()) {
            gameState.shuffleDeck(player);
            for (int i = 0; i < game.getFormat().getHandSize(); i++)
                gameState.playerDrawsCard(player);
        }
        if (game.getFormat().hasMulliganRule())
            _followingGameProcess = new MulliganProcess(game.getGameState().getPlayerOrder().getClockwisePlayOrder(_firstPlayer, false));
        else
            _followingGameProcess = new TribblesStartOfTurnGameProcess();

    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
