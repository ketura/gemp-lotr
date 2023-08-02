package com.gempukku.lotro.processes;

import com.gempukku.lotro.game.DefaultGame;

public interface GameProcess {
    <AbstractGame extends DefaultGame> void process(AbstractGame game);

    GameProcess getNextProcess();
}