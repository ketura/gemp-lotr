package com.gempukku.lotro.logic.timing.processes;

import com.gempukku.lotro.game.state.LotroGame;

public interface GameProcess {
    public void process(LotroGame game);

    public GameProcess getNextProcess();
}
