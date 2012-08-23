package com.gempukku.lotro.db;

import com.gempukku.lotro.game.Player;

import java.util.Collection;

public interface LeagueParticipationDAO {
    public void userJoinsLeague(String leagueId, Player player, String remoteAddr);

    public Collection<String> getUsersParticipating(String leagueId);
}
