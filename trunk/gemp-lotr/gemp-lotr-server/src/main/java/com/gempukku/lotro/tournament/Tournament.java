package com.gempukku.lotro.tournament;

import com.gempukku.lotro.competitive.PlayerStanding;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.LotroFormat;

import java.util.List;

public interface Tournament {
    public CollectionType getCollectionType();

    public LotroFormat getLotroFormat();

    public String getTournamentName();

    public List<PlayerStanding> getCurrentStandings();

    public int getCurrentRound();

    public void advanceTournament(TournamentCallback tournamentCallback);

    public void reportGameFinished(TournamentCallback tournamentCallback, String winner, String loser);

    public void dropPlayer(String player);

    public boolean isFinished();

    public boolean isPlayerCompeting(String player);
}
