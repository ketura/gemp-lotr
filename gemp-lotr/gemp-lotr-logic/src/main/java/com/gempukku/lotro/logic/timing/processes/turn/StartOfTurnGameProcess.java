package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.effects.TriggeringResultEffect;
import com.gempukku.lotro.logic.timing.actions.SimpleEffectAction;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.results.StartOfTurnResult;

public class StartOfTurnGameProcess implements GameProcess {
    private LotroGame _game;

    public StartOfTurnGameProcess(LotroGame game) {
        _game = game;
    }

    @Override
    public void process(LotroGame game) {
        PlayOrder playOrder = _game.getGameState().getPlayerOrder().getClockwisePlayOrder(_game.getGameState().getCurrentPlayerId(), false);
        playOrder.getNextPlayer();

        String nextPlayer = playOrder.getNextPlayer();
        _game.getGameState().setCurrentPhase(Phase.BETWEEN_TURNS);
        _game.getGameState().startPlayerTurn(nextPlayer);
        _game.getGameState().startAffectingCardsForCurrentPlayer(_game);

        _game.getActionsEnvironment().addActionToStack(new SimpleEffectAction(new TriggeringResultEffect(new StartOfTurnResult(), "Start of turn"), "Start of turn"));
    }

    @Override
    public GameProcess getNextProcess() {
        return new FellowshipGameProcess(_game);
    }
}
