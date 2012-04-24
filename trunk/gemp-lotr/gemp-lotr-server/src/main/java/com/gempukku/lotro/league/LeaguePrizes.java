package com.gempukku.lotro.league;

import com.gempukku.lotro.game.CardCollection;

public interface LeaguePrizes {
    public CardCollection getPrizeForLeagueMatchWinner(int winCountThisSerie, int totalGamesPlayedThisSerie, String format);

    public CardCollection getPrizeForLeagueMatchLoser(int winCountThisSerie, int totalGamesPlayedThisSerie, String format);

    public CardCollection getPrizeForLeague(int position, int playersCount, float multiplier, String format);
}
