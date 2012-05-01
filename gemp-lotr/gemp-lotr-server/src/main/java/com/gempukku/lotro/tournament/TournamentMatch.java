package com.gempukku.lotro.tournament;

public class TournamentMatch {
    private String _playerOne;
    private String _playerTwo;
    private String _winner;
    private int _round;

    public TournamentMatch(String playerOne, String playerTwo, String winner, int round) {
        _playerOne = playerOne;
        _playerTwo = playerTwo;
        _winner = winner;
        _round = round;
    }

    public String getPlayerOne() {
        return _playerOne;
    }

    public String getPlayerTwo() {
        return _playerTwo;
    }

    public String getWinner() {
        return _winner;
    }

    public int getRound() {
        return _round;
    }
}
