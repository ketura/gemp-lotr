package com.gempukku.lotro.league;

import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.CardCollection;

public interface LeagueSerieData {
    public int getStart();

    public int getEnd();

    public int getMaxMatches();

    public boolean isLimited();

    public String getName();

    public String getFormat();

    public CollectionType getCollectionType();

    public CardCollection getPrizeForLeagueMatchWinner(int winCountThisSerie, int totalGamesPlayedThisSerie);

    public CardCollection getPrizeForLeagueMatchLoser(int winCountThisSerie, int totalGamesPlayedThisSerie);
}
