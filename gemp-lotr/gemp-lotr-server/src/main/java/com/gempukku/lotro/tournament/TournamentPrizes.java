package com.gempukku.lotro.tournament;

import com.gempukku.lotro.competitive.PlayerStanding;
import com.gempukku.lotro.game.CardCollection;

public interface TournamentPrizes {
    public CardCollection getPrizeForTournament(PlayerStanding playerStanding, int playersCount);
    public String getRegistryRepresentation();
    public String getPrizeDescription();
}
