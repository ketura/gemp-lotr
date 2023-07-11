package com.gempukku.lotro.tournament;

import com.gempukku.lotro.cards.LotroCardBlueprintLibrary;

public class TournamentPrizeSchemeRegistry {
    public TournamentPrizes getTournamentPrizes(LotroCardBlueprintLibrary library, String prizesScheme) {
        if (prizesScheme == null || prizesScheme.equals("none"))
            return new NoPrizes();

        if (prizesScheme.equals("onDemand"))
            return new SingleEliminationOnDemandPrizes(library, "onDemand");

        if (prizesScheme.equals("daily"))
            return new DailyTournamentPrizes(library, "daily");

        return null;
    }
}
