package com.gempukku.lotro.league;

import com.gempukku.lotro.db.DbAccess;
import com.gempukku.lotro.db.LeagueDAO;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;

public class LeagueService {
    private LeagueDAO _leagueDao;

    public LeagueService(DbAccess dbAccess, LotroCardBlueprintLibrary library) {
        _leagueDao = new LeagueDAO(dbAccess, library);
    }
}
