package com.gempukku.lotro.logic.timing.processes.pregame;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class PlayStartingFellowshipGameProcess implements GameProcess {
    private LotroGame _game;
    private PlayOrder _playOrder;

    private GameProcess _nextProcess;

    public PlayStartingFellowshipGameProcess(LotroGame game, PlayOrder playOrder) {
        _game = game;
        _playOrder = playOrder;
    }

    @Override
    public void process() {
        String nextPlayer = _playOrder.getNextPlayer();
        if (nextPlayer != null) {
            _game.getGameState().startPlayerTurn(nextPlayer);
            _nextProcess = new PlayerPlaysStartingFellowshipGameProcess(_game, nextPlayer, new PlayStartingFellowshipGameProcess(_game, _playOrder));
        } else {
            _nextProcess = new PlayersDrawEightCardsGameProcess(_game);
        }
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
