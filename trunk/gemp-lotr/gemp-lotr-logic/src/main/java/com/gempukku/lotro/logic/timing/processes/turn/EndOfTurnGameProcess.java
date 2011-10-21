package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.TriggeringResultEffect;
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
        _game.getActionsEnvironment().addActionToStack(new SimpleEffectAction(new TriggeringResultEffect(new EndOfTurnResult(), "End of turn"), "End of turn"));
    }

    @Override
    public GameProcess getNextProcess() {
        return new CleanupProcess(_game);
    }
}
