package com.gempukku.lotro.logic.timing.processes.pregame;

import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.StartOfTurnGameProcess;

public class PlayersDrawEightCardsGameProcess implements GameProcess {
    private LotroGame _game;
    private String _firstPlayer;

    public PlayersDrawEightCardsGameProcess(LotroGame game, String firstPlayer) {
        _game = game;
        _firstPlayer = firstPlayer;
    }

    @Override
    public void process() {
        GameState gameState = _game.getGameState();
        for (String player : gameState.getPlayerOrder().getAllPlayers()) {
            gameState.shuffleDeck(player);
            for (int i = 0; i < 8; i++)
                gameState.playerDrawsCard(player);
        }
    }

    @Override
    public GameProcess getNextProcess() {
        if (_game.getFormat().hasMulliganRule())
            return new MulliganProcess(_game, _game.getGameState().getPlayerOrder().getClockwisePlayOrder(_firstPlayer, false));
        else
            return new StartOfTurnGameProcess(_game);
    }
}
