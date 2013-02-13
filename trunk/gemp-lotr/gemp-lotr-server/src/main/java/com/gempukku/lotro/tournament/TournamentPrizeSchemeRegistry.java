package com.gempukku.lotro.tournament;

import com.gempukku.lotro.cards.CardSets;

public class TournamentPrizeSchemeRegistry {
    public TournamentPrizes getTournamentPrizes(CardSets cardSets, String prizesScheme) {
        if (prizesScheme == null || prizesScheme.equals("none"))
            return new NoPrizes();
        
        if (prizesScheme.equals("onDemand"))
            return new SingleEliminationOnDemandPrizes(cardSets, "onDemand");

        if (prizesScheme.equals("daily"))
            return new DailyTournamentPrizes("daily");

        return null;
    }
}
