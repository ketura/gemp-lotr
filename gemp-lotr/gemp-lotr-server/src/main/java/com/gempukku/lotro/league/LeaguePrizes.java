package com.gempukku.lotro.league;

import com.gempukku.lotro.game.CardCollection;

public interface LeaguePrizes {
    public CardCollection getPrizeForLeagueMatchWinner(int winCountThisSerie, int totalGamesPlayedThisSerie);

    public CardCollection getPrizeForLeagueMatchLoser(int winCountThisSerie, int totalGamesPlayedThisSerie);

    public CardCollection getPrizeForLeague(int position, int playersCount, float multiplier);
}
