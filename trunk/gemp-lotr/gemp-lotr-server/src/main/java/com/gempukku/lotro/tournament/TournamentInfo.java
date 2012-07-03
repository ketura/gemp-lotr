package com.gempukku.lotro.tournament;

import java.util.Date;

public class TournamentInfo {
    private String _tournamentId;
    private String _tournamentClass;
    private String _parameters;
    private Date _start;

    public TournamentInfo(String tournamentId, String tournamentClass, String parameters, Date start) {
        this._tournamentId = tournamentId;
        this._tournamentClass = tournamentClass;
        this._parameters = parameters;
        this._start = start;
    }

    public String getTournamentId() {
        return _tournamentId;
    }

    public String getTournamentClass() {
        return _tournamentClass;
    }

    public String getParameters() {
        return _parameters;
    }

    public Date getStart() {
        return _start;
    }
}
