package com.gempukku.lotro.logic.timing.processes;

public interface GameProcess {
    public void process();

    public GameProcess getNextProcess();
}
