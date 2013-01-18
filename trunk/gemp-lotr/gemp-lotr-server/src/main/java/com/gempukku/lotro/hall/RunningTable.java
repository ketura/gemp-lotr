package com.gempukku.lotro.hall;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.league.LeagueSerieData;

public class RunningTable {
    private String _gameId;
    private String _formatName;
    private String _tournamentName;
    private League _league;
    private LeagueSerieData _leagueSerie;

    public RunningTable(String gameId, String formatName, String tournamentName, League league, LeagueSerieData leagueSerie) {
        _gameId = gameId;
        _formatName = formatName;
        _tournamentName = tournamentName;
        _league = league;
        _leagueSerie = leagueSerie;
    }

    public String getFormatName() {
        return _formatName;
    }

    public String getGameId() {
        return _gameId;
    }

    public String getTournamentName() {
        return _tournamentName;
    }

    public League getLeague() {
        return _league;
    }

    public LeagueSerieData getLeagueSerie() {
        return _leagueSerie;
    }
}
