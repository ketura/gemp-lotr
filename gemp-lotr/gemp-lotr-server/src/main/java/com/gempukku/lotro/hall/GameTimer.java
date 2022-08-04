package com.gempukku.lotro.hall;

public class GameTimer {
    private final boolean longGame;
    private final String name;
    private final int maxSecondsPerPlayer;
    private final int maxSecondsPerDecision;

    public GameTimer(boolean longGame, String name, int maxSecondsPerPlayer, int maxSecondsPerDecision) {
        this.longGame = longGame;
        this.name = name;
        this.maxSecondsPerPlayer = maxSecondsPerPlayer;
        this.maxSecondsPerDecision = maxSecondsPerDecision;
    }

    public boolean isLongGame() {
        return longGame;
    }

    public String getName() {
        return name;
    }

    public int getMaxSecondsPerPlayer() {
        return maxSecondsPerPlayer;
    }

    public int getMaxSecondsPerDecision() {
        return maxSecondsPerDecision;
    }
}
