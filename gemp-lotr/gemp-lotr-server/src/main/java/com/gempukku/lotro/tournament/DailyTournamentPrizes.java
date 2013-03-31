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
        if (playerStanding.getStanding() == 1) {
            tournamentPrize.addItem("(S)Booster Choice", 10);
        } else if (playerStanding.getStanding() == 2) {
            tournamentPrize.addItem("(S)Booster Choice", 8);
        } else if (playerStanding.getStanding() <=4) {
            tournamentPrize.addItem("(S)Booster Choice", 5);
        } else if (playerStanding.getStanding()<=8) {
            tournamentPrize.addItem("(S)Booster Choice", 2);
        }

        if (tournamentPrize.getAll().size() == 0)
            return null;
        return tournamentPrize;
    }

    @Override
    public String getRegistryRepresentation() {
        return _registryRepresentation;
    }

    @Override
    public String getPrizeDescription() {
        return "<div class='prizeHint' value='1st place - 10 boosters, 2nd place - 8 boosters, 3rd and 4th place - 5 boosters, 5th-8th - 2 boosters each'>10-8-5-2</div>";
    }
}
