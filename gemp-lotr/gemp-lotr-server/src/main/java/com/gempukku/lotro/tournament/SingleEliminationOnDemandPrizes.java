package com.gempukku.lotro.tournament;

import com.gempukku.lotro.cards.CardSets;
import com.gempukku.lotro.cards.packs.SetDefinition;
import com.gempukku.lotro.competitive.PlayerStanding;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SingleEliminationOnDemandPrizes implements TournamentPrizes{
    private List<String> _promos = new ArrayList<String>();
    private String _registryRepresentation;

    public SingleEliminationOnDemandPrizes(CardSets cardSets, String registryRepresentation) {
        _registryRepresentation = registryRepresentation;
        for (SetDefinition setDefinition : cardSets.getSetDefinitions().values()) {
            if (setDefinition.hasFlag("originalSet"))
                _promos.addAll(setDefinition.getCardsOfRarity("P"));
        }
    }

    @Override
    public CardCollection getPrizeForTournament(PlayerStanding playerStanding, int playersCount) {
        DefaultCardCollection tournamentPrize = new DefaultCardCollection();
        if (playerStanding.getPoints() == 6) {
            tournamentPrize.addItem("(S)Booster Choice", 3);
            tournamentPrize.addItem(getRandom(_promos), 1);
        } else if (playerStanding.getPoints() == 5) {
            tournamentPrize.addItem("(S)Booster Choice", 2);
            tournamentPrize.addItem(getRandom(_promos), 1);
        } else if (playerStanding.getPoints() == 3) {
            tournamentPrize.addItem("(S)Booster Choice", 1);
            tournamentPrize.addItem(getRandom(_promos), 1);
        } else {
            tournamentPrize.addItem("(S)Booster Choice", 1);
        }

        if (tournamentPrize.getAll().size() == 0)
            return null;
        return tournamentPrize;
    }

    @Override
    public CardCollection getTrophyForTournament(PlayerStanding playerStanding, int playersCount) {
        return null;
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
        return "<div class='prizeHint' value='3 wins - 3 boosters and a random promo, 2 wins - 2 boosters and a random promo, 1 win - 1 booster and a random promo, 0 wins - 1 booster'>(3+promo)-(2+promo)-(1+promo)-1</div>";
    }
}
