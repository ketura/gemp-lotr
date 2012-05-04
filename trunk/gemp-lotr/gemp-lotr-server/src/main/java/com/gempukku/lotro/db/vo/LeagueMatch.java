package com.gempukku.lotro.db.vo;

public class LeagueMatch {
    private String _winner;
    private String _loser;
    private String _serie;

    public LeagueMatch(String serie, String winner, String loser) {
        _serie = serie;
        _winner = winner;
        _loser = loser;
    }

    public String getSerie() {
        return _serie;
    }

    public String getLoser() {
        return _loser;
    }

    public String getWinner() {
        return _winner;
    }
}
