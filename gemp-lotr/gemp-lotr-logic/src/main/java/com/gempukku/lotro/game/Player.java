package com.gempukku.lotro.game;

public class Player {

    private final String _playerId;
    private boolean _decked;
    private int _score;

    public Player(String playerId) {
        _playerId = playerId;
        _decked = false;
        _score = 0;
    }

    public String getPlayerId() {
        return _playerId;
    }

    public boolean getDecked() {
        return _decked;
    }
    public void setDecked(boolean decked) {
        _decked = decked;
    }

    public void scorePoints(int points) {
        _score += points;
    }

    public int getScore() {
        return _score;
    }

}
