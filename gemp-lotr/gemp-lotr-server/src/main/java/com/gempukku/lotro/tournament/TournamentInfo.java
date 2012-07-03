package com.gempukku.lotro.tournament;

import java.util.Date;

public class TournamentInfo {
    private int cost;
    private String tournamentId;
    private String tournamentClass;
    private String parameters;
    private Date start;

    public TournamentInfo(int cost, String tournamentId, String tournamentClass, String parameters, Date start) {
        this.cost = cost;
        this.tournamentId = tournamentId;
        this.tournamentClass = tournamentClass;
        this.parameters = parameters;
        this.start = start;
    }

    public int getCost() {
        return cost;
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public String getTournamentClass() {
        return tournamentClass;
    }

    public String getParameters() {
        return parameters;
    }

    public Date getStart() {
        return start;
    }
}
