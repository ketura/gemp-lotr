package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.Player;

import java.util.Collection;

public interface LeagueParticipationDAO {
    public void userJoinsLeague(League league, Player player);

    public Collection<String> getUsersParticipating(League league);
}
