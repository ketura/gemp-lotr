package com.gempukku.lotro.logic.timing.processes.pregame;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class PlayStartingFellowshipGameProcess implements GameProcess {
    private LotroGame _game;
    private PlayOrder _playOrder;
    private String _firstPlayer;

    private GameProcess _nextProcess;

    public PlayStartingFellowshipGameProcess(LotroGame game, PlayOrder playOrder, String firstPlayer) {
        _game = game;
        _playOrder = playOrder;
        _firstPlayer = firstPlayer;
    }

    @Override
    public void process() {
        String nextPlayer = _playOrder.getNextPlayer();

        _game.getGameState().stopAffectingCardsForCurrentPlayer();

        if (nextPlayer != null) {
            _game.getGameState().startPlayerTurn(nextPlayer);
            _game.getGameState().startAffectingCardsForCurrentPlayer(_game);

            _nextProcess = new PlayerPlaysStartingFellowshipGameProcess(_game, nextPlayer, new PlayStartingFellowshipGameProcess(_game, _playOrder, _firstPlayer));
        } else {
            _nextProcess = new PlayersDrawEightCardsGameProcess(_game, _firstPlayer);
        }
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
