package com.gempukku.lotro.tournament;

import com.gempukku.lotro.competitive.PlayerStanding;
import com.gempukku.lotro.db.vo.CollectionType;

import java.util.List;

public interface Tournament {
    public String getTournamentId();

    public CollectionType getCollectionType();

    public String getFormat();

    public String getTournamentName();

    public List<PlayerStanding> getCurrentStandings();

    public int getCurrentRound();

    public void advanceTournament(TournamentCallback tournamentCallback);

    public void reportGameFinished(TournamentCallback tournamentCallback, String winner, String loser);

    public void dropPlayer(String player);

    public boolean isFinished();

    public boolean isPlayerCompeting(String player);
}
