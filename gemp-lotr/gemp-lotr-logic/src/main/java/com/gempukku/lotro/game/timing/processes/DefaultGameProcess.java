package com.gempukku.lotro.game.timing.processes;

import com.gempukku.lotro.game.DefaultGame;

public abstract class DefaultGameProcess<AbstractGame> implements GameProcess {
    public void process(DefaultGame game) {
        AbstractGame abstractGame = (AbstractGame) game;
        process(abstractGame);
    }

    public abstract void process(AbstractGame game);

    public GameProcess getNextProcess() { return null; }
}