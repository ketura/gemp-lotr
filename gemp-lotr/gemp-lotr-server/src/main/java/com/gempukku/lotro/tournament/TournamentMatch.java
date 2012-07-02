package com.gempukku.lotro.tournament;

import com.gempukku.lotro.competitive.CompetitiveMatchResult;

public class TournamentMatch implements CompetitiveMatchResult {
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

    @Override
    public String getWinner() {
        return _winner;
    }

    @Override
    public String getLoser() {
        if (_playerOne.equals(_winner))
            return _playerTwo;
        else
            return _playerOne;
    }

    public boolean isBye() {
        return getLoser().equals("Bye");
    }

    public int getRound() {
        return _round;
    }
}
