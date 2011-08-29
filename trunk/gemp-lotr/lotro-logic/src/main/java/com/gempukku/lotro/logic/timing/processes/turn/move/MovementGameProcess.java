package com.gempukku.lotro.logic.timing.processes.turn.move;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class MovementGameProcess implements GameProcess {
    private LotroGame _game;
    private GameProcess _afterMovementGameProcess;

    public MovementGameProcess(LotroGame game, GameProcess afterMovementGameProcess) {
        _game = game;
        _afterMovementGameProcess = afterMovementGameProcess;
    }

    @Override
    public void process() {

    }

    @Override
    public GameProcess getNextProcess() {
        return new PlayerPlaysNextSiteIfNotThereGameProcess(_game, _afterMovementGameProcess);
    }
}
