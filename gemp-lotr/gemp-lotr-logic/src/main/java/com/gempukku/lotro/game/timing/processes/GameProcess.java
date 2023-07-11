package com.gempukku.lotro.game.timing.processes;

import com.gempukku.lotro.game.DefaultGame;

public interface GameProcess {
    public void process(DefaultGame game);

    public GameProcess getNextProcess();
}
