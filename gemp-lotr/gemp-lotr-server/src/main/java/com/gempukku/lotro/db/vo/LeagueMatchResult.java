package com.gempukku.lotro.db.vo;

import com.gempukku.lotro.competitive.CompetitiveMatchResult;

public class LeagueMatchResult implements CompetitiveMatchResult {
    private final String _winner;
    private final String _loser;
    private final String _serieName;

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
