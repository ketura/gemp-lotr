package com.gempukku.lotro.db;

import com.gempukku.lotro.game.User;

import java.util.Collection;

public interface LeagueParticipationDAO {
    public void userJoinsLeague(String leagueId, User player, String remoteAddr);

    public Collection<String> getUsersParticipating(String leagueId);
}
