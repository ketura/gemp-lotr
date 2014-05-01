package com.gempukku.lotro.tournament;

import com.gempukku.lotro.competitive.PlayerStanding;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;

public class DailyTournamentPrizes implements TournamentPrizes {
    private String _registryRepresentation;

    public DailyTournamentPrizes(String registryRepresentation) {
        _registryRepresentation = registryRepresentation;
    }

    @Override
    public CardCollection getPrizeForTournament(PlayerStanding playerStanding, int playersCount) {
        DefaultCardCollection tournamentPrize = new DefaultCardCollection();
        tournamentPrize.addItem("(S)Booster Choice", (playerStanding.getPlayerWins() + playerStanding.getPlayerByes()) * 2);

        if (tournamentPrize.getAll().size() == 0) {
            return null;
        }
        return tournamentPrize;
    }

    @Override
    public CardCollection getTrophyForTournament(PlayerStanding playerStanding, int playersCount) {
        return null;
    }

    @Override
    public String getRegistryRepresentation() {
        return _registryRepresentation;
    }

    @Override
    public String getPrizeDescription() {
        return "2 boosters per win (or bye), max 3 rounds";
    }
}
