package com.gempukku.lotro.tournament;

import com.gempukku.lotro.cards.packs.RarityReader;
import com.gempukku.lotro.cards.packs.SetRarity;
import com.gempukku.lotro.competitive.PlayerStanding;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SingleEliminationOnDemandPrizes implements TournamentPrizes{
    private List<String> _promos = new ArrayList<String>();
    private String _registryRepresentation;

    public SingleEliminationOnDemandPrizes(String registryRepresentation) {
        _registryRepresentation = registryRepresentation;
        RarityReader rarityReader = new RarityReader();

        for (int i = 0; i <= 19; i++) {
            SetRarity setRarity = rarityReader.getSetRarity(String.valueOf(i));
            _promos.addAll(setRarity.getCardsOfRarity("P"));
        }

    }

    @Override
    public CardCollection getPrizeForTournament(PlayerStanding playerStanding, int playersCount) {
        DefaultCardCollection tournamentPrize = new DefaultCardCollection();
        if (playerStanding.getPoints() == 3) {
            tournamentPrize.addItem("(S)Booster Choice", 2);
        } else if (playerStanding.getPoints() == 2) {
            tournamentPrize.addItem("(S)Booster Choice", 1);
            tournamentPrize.addItem(getRandom(_promos), 1);
        } else if (playerStanding.getPoints() == 1) {
            tournamentPrize.addItem("(S)Booster Choice", 1);
        }

        if (tournamentPrize.getAll().size() == 0)
            return null;
        return tournamentPrize;
    }

    private String getRandom(List<String> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    @Override
    public String getRegistryRepresentation() {
        return _registryRepresentation;
    }

    @Override
    public String getPrizeDescription() {
        return "1st place - 2 boosters, 2nd place - 1 booster and a random promo, 3rd & 4th - 1 booster";
    }
}
