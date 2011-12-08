package com.gempukku.lotro.db.vo;

public class LeagueMatch {
    private String _winner;
    private String _loser;

    public LeagueMatch(String winner, String loser) {
        _winner = winner;
        _loser = loser;
    }

    public String getLoser() {
        return _loser;
    }

    public String getWinner() {
        return _winner;
    }
}
