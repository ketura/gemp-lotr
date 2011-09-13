package com.gempukku.lotro.logic.timing.processes.pregame;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

import java.util.List;

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

        if (_game.getGameState().getCurrentPlayerId() != null)
            _game.getGameState().stopAffectingCardsForCurrentPlayer();

        if (nextPlayer != null) {
            _game.getGameState().startPlayerTurn(nextPlayer);

            PhysicalCard ringBearer = _game.getGameState().getRingBearer(nextPlayer);
            _game.getGameState().startAffecting(ringBearer, _game.getModifiersEnvironment());

            List<PhysicalCard> attachedToRingBearer = _game.getGameState().getAttachedCards(ringBearer);
            for (PhysicalCard physicalCard : attachedToRingBearer)
                _game.getGameState().startAffecting(physicalCard, _game.getModifiersEnvironment());

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
