package com.gempukku.lotro.hall;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.LotroGameMediator;
import com.gempukku.lotro.league.LeagueSerieData;

public class RunningTable {
    private LotroGameMediator _lotroGameMediator;
    private String _formatName;
    private String _tournamentName;
    private League _league;
    private LeagueSerieData _leagueSerie;

    public RunningTable(LotroGameMediator lotroGameMediator, String formatName, String tournamentName, League league, LeagueSerieData leagueSerie) {
        _lotroGameMediator = lotroGameMediator;
        _formatName = formatName;
        _tournamentName = tournamentName;
        _league = league;
        _leagueSerie = leagueSerie;
    }

    public String getFormatName() {
        return _formatName;
    }

    public LotroGameMediator getLotroGameMediator() {
        return _lotroGameMediator;
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
