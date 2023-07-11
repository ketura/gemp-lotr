package com.gempukku.lotro.collection;

import com.gempukku.lotro.db.DbAccess;
import com.gempukku.lotro.db.DbCollectionDAO;
import com.gempukku.lotro.db.DbPlayerDAO;
import com.gempukku.lotro.cards.CardNotFoundException;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;

import java.io.IOException;
import java.sql.SQLException;

// Tidings of Erebor
public class NewCollectionManagerTests
{
    private static DbAccess dbAccess = new DbAccess();
    //private static DbAccess dbAccess = new DbAccess("jdbc:mysql://localhost:35001/gemp_db",
    //            "root", "rootpass", false);

    //@Test
    public void ConvertCollectionTest() throws DecisionResultInvalidException, CardNotFoundException, IOException, SQLException {
        var collDAO = new DbCollectionDAO(dbAccess, new CollectionSerializer());

        collDAO.convertCollection(31040, "trophy");
    }

    //This is probably never needed anymore now that it's been ran once
    //@Test
    public void ConvertAllPlayerCollectionsTest() throws IOException, SQLException {
        var collDAO = new DbCollectionDAO(dbAccess, new CollectionSerializer());
        var playerDAO = new DbPlayerDAO(dbAccess);

        var players = playerDAO.getAllPlayers();

        for (var player : players) {
            System.out.println("Processing player: " + player.name);
            var collections = collDAO.getAllCollectionsForPlayer(player.id);
            for(var coll : collections) {
                System.out.println("\t" + coll.type);
                collDAO.convertCollection(player.id, coll.type);
            }
        }
    }

//    @ParameterizedTest(name = "{0} in getPlayerCollectionsByType matches getPlayerCollectionsByType2.")
//    @ValueSource(strings = {
//            "1607391235747",
//            "1608854633002",
//            "1609944243486",
//            "1610556687100",
//            "1611015929599",
//            "1611889855612",
//            "1612972708206",
//            "1614003645826",
//            "1614210977555",
//            "1614814220508",
//            "1661759490576",
//            "1661759546343",
//            "1662624726804",
//            "1662848820658",
//            "1662850297644",
//            "1662929225417",
//            "1665904004527",
//            "1665904184460",
//            "1665907057170",
//            "trophy",
//            "permanent",
//
//    })
//    public void CompareAllPlayers(String type) throws IOException, SQLException {
//        var collDAO = new DbCollectionDAO(dbAccess, new CollectionSerializer());
//        var playerDAO = new DbPlayerDAO(dbAccess);
//
//        var map1 = collDAO.getPlayerCollectionsByType(type);
//        var map2 = collDAO.getPlayerCollectionsByType2(type);
//
//        var players = playerDAO.getAllPlayers();
//
//        var keys = Stream.concat(map1.keySet().stream(), map2.keySet().stream())
//            .distinct()
//            .toList();
//
//        for(int key : keys) {
//            assertEquals(map1.get(key), map2.get(key));
//        }
//    }



}