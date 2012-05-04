package com.gempukku.lotro.db.vo;

import com.gempukku.lotro.competitive.CompetitiveMatchResult;

public class LeagueMatchResult implements CompetitiveMatchResult {
    private String _winner;
    private String _loser;
    private String _serieName;

    public LeagueMatchResult(String serieName, String winner, String loser) {
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
