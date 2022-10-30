package com.gempukku.lotro.collection;

import com.gempukku.lotro.db.DbAccess;
import com.gempukku.lotro.db.DbCollectionDAO;
import com.gempukku.lotro.db.DbPlayerDAO;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

// Tidings of Erebor
public class NewCollectionManagerTests
{
    private static DbAccess dbAccess = new DbAccess("jdbc:mysql://localhost:35001/gemp_db",
            "root", "rootpass", false);

    @Test
    public void ConvertCollectionTest() throws DecisionResultInvalidException, CardNotFoundException, IOException, SQLException {
        var collDAO = new DbCollectionDAO(dbAccess, new CollectionSerializer());

        collDAO.convertCollection(31040, "trophy");
    }

    @Test
    public void ConvertAllPlayerCollectionsTest() throws DecisionResultInvalidException, CardNotFoundException, IOException, SQLException {
        var collDAO = new DbCollectionDAO(dbAccess, new CollectionSerializer());
        var playerDAO = new DbPlayerDAO(dbAccess);

        var players = playerDAO.getAllPlayers();

        for (var player : players) {
            var collections = collDAO.getAllCollectionsForPlayer(player.id);
            for(var coll : collections) {
                collDAO.convertCollection(player.id, coll.type);
            }

        }


    }



}