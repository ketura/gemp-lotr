package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.TriggeringEffect;
import com.gempukku.lotro.logic.timing.actions.SimpleEffectAction;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.results.EndOfTurnResult;

public class EndOfTurnGameProcess implements GameProcess {
    private LotroGame _game;

    public EndOfTurnGameProcess(LotroGame game) {
        _game = game;
    }

    @Override
    public void process() {
        _game.getActionsEnvironment().addActionToStack(new SimpleEffectAction(new TriggeringEffect(new EndOfTurnResult()), "End of turn"));
        _game.getGameState().stopAffectingCardsForCurrentPlayer();
        _game.getGameState().setCurrentPhase(Phase.BETWEEN_TURNS);
    }

    @Override
    public GameProcess getNextProcess() {
        return new StartOfTurnGameProcess(_game);
    }
}
