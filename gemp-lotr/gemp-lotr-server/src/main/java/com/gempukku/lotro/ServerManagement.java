package com.gempukku.lotro;

import com.gempukku.lotro.db.DbAccess;
import com.gempukku.lotro.db.LeagueDAO;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.DefaultCardCollection;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;

import java.io.IOException;
import java.sql.SQLException;

public class ServerManagement {
    public static void main(String[] args) throws Exception {
        DbAccess dbAccess = new DbAccess();
        LotroCardBlueprintLibrary library = new LotroCardBlueprintLibrary();

        addLeague(dbAccess, library);
//        League league = getLeague(dbAccess, library, "league_test");
//        CardCollection leagueCollection = league.getBaseCollection();
//        List<CardCollection.Item> items = leagueCollection.getItems("");
//        System.out.println(items.size());
    }

    private static void addLeague(DbAccess dbAccess, LotroCardBlueprintLibrary library) throws IOException, SQLException {
        LeagueDAO leagueDao = new LeagueDAO(dbAccess, library);
        DefaultCardCollection collection = new DefaultCardCollection(library);
        collection.addPacks("Fellowship of the Ring - League", 12);
        collection.finishedReading();

        leagueDao.addLeague("Test league", "league_test", collection, 20111122, 20121123);
    }

    private static League getLeague(DbAccess dbAccess, LotroCardBlueprintLibrary library, String leagueType) {
        LeagueDAO leagueDao = new LeagueDAO(dbAccess, library);
        for (League league : leagueDao.getActiveLeagues()) {
            if (league.getType().equals(leagueType))
                return league;
        }
        return null;
    }
}
