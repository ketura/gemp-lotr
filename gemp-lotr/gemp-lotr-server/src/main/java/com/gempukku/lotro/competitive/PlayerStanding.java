package com.gempukku.lotro.competitive;

public class PlayerStanding {
    private String _playerName;
    private int _points;
    private int _gamesPlayed;
    private int _playerWins;
    private int _playerLosses;
    private int _playerByes;
    private float _opponentWin;
    private int _standing;

    public PlayerStanding(String playerName, int points, int gamesPlayed, int playerWins, int playerLosses, int playerByes) {
        _playerName = playerName;
        _points = points;
        _gamesPlayed = gamesPlayed;
        _playerWins = playerWins;
        _playerLosses = playerLosses;
        _playerByes = playerByes;
    }

    public int getGamesPlayed() {
        return _gamesPlayed;
    }

    public float getOpponentWin() {
        return _opponentWin;
    }

    public String getPlayerName() {
        return _playerName;
    }

    public int getPoints() {
        return _points;
    }

    public int getStanding() {
        return _standing;
    }

    public int getPlayerWins() {
        return _playerWins;
    }

    public int getPlayerLosses() {
        return _playerLosses;
    }

    public int getPlayerByes() {
        return _playerByes;
    }

    public void setOpponentWin(float opponentWin) {
        _opponentWin = opponentWin;
    }

    public void setStanding(int standing) {
        _standing = standing;
    }
}
