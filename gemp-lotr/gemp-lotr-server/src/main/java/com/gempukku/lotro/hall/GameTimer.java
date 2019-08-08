package com.gempukku.lotro.hall;

public class GameTimer {
    private boolean longGame;
    private String name;
    private int maxSecondsPerPlayer;
    private int maxSecondsPerDecision;

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
