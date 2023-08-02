package com.gempukku.lotro.processes.lotronly;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.PlayOrder;
import com.gempukku.lotro.processes.GameProcess;
import com.gempukku.lotro.processes.PlayersDrawStartingHandGameProcess;

public class PlayStartingFellowshipGameProcess implements GameProcess {
    private final PlayOrder _playOrder;
    private final String _firstPlayer;

    private GameProcess _nextProcess;

    public PlayStartingFellowshipGameProcess(PlayOrder playOrder, String firstPlayer) {
        _playOrder = playOrder;
        _firstPlayer = firstPlayer;
    }

    @Override
    public void process(DefaultGame game) {
        String nextPlayer = _playOrder.getNextPlayer();

        if (nextPlayer != null) {
            String currentPlayer = game.getGameState().getCurrentPlayerId();
            if (currentPlayer != null && currentPlayer.equals(_playOrder.getFirstPlayer()))
                game.getGameState().stopAffectingCardsForCurrentPlayer();
            game.getGameState().startPlayerTurn(nextPlayer);
            game.getGameState().startAffectingCardsForCurrentPlayer(game);

            _nextProcess = new PlayerPlaysStartingFellowshipGameProcess(nextPlayer, new PlayStartingFellowshipGameProcess(_playOrder, _firstPlayer));
        } else {
            game.getGameState().stopAffectingCardsForCurrentPlayer();
            _nextProcess = new PlayersDrawStartingHandGameProcess(_firstPlayer);
        }
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
