package com.gempukku.lotro.db.vo;

public class LeagueMatch {
    private String _winner;
    private String _loser;
    private String _serieName;

    public LeagueMatch(String serieName, String winner, String loser) {
        _serieName = serieName;
        _winner = winner;
        _loser = loser;
    }

    public String getSerieName() {
        return _serieName;
    }

    public String getLoser() {
        return _loser;
    }

    public String getWinner() {
        return _winner;
    }
}
