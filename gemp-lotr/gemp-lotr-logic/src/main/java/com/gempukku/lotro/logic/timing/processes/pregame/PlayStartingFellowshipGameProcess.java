package com.gempukku.lotro.logic.timing.processes.pregame;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class PlayStartingFellowshipGameProcess implements GameProcess {
    private PlayOrder _playOrder;
    private String _firstPlayer;

    private GameProcess _nextProcess;

    public PlayStartingFellowshipGameProcess(PlayOrder playOrder, String firstPlayer) {
        _playOrder = playOrder;
        _firstPlayer = firstPlayer;
    }

    @Override
    public void process(LotroGame game) {
        String nextPlayer = _playOrder.getNextPlayer();

        game.getGameState().stopAffectingCardsForCurrentPlayer();

        if (nextPlayer != null) {
            game.getGameState().startPlayerTurn(nextPlayer);
            game.getGameState().startAffectingCardsForCurrentPlayer(game);

            _nextProcess = new PlayerPlaysStartingFellowshipGameProcess(game, nextPlayer, new PlayStartingFellowshipGameProcess(_playOrder, _firstPlayer));
        } else {
            _nextProcess = new PlayersDrawEightCardsGameProcess(game, _firstPlayer);
        }
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
