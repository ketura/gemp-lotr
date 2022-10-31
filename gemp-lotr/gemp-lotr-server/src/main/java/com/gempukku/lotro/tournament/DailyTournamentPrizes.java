package com.gempukku.lotro.tournament;

import com.gempukku.lotro.competitive.PlayerStanding;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.packs.SetDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DailyTournamentPrizes implements TournamentPrizes {
    private final List<String> _promos = new ArrayList<>();
    private final String _registryRepresentation;

    public DailyTournamentPrizes(LotroCardBlueprintLibrary library, String registryRepresentation) {
        _registryRepresentation = registryRepresentation;
        for (SetDefinition setDefinition : library.getSetDefinitions().values()) {
            if (setDefinition.hasFlag("originalSet"))
                _promos.addAll(setDefinition.getCardsOfRarity("P"));
        }
    }

    @Override
    public CardCollection getPrizeForTournament(PlayerStanding playerStanding, int playersCount) {
        DefaultCardCollection tournamentPrize = new DefaultCardCollection();
        tournamentPrize.addItem("(S)All Decipher Choice - Booster", playerStanding.getPoints());
        if (playerStanding.getPlayerWins() + playerStanding.getPlayerByes() >= 2)
            tournamentPrize.addItem(getRandom(_promos), 1);

        if (!tournamentPrize.getAll().iterator().hasNext()) {
            return null;
        }
        return tournamentPrize;
    }

    @Override
    public CardCollection getTrophyForTournament(PlayerStanding playerStanding, int playersCount) {
        return null;
    }

    private String getRandom(List<String> list) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    @Override
    public String getRegistryRepresentation() {
        return _registryRepresentation;
    }

    @Override
    public String getPrizeDescription() {
        return "2 boosters per win (or bye), 1 per loss, max 3 rounds, players with at least 2 wins get a promo";
    }
}
