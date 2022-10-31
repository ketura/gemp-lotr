package com.gempukku.lotro.db;

import com.gempukku.lotro.common.DBDefs;
import com.gempukku.lotro.game.CardCollection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CollectionDAO {
    public Map<Integer, CardCollection> getPlayerCollectionsByType(String type) throws SQLException, IOException;

    public CardCollection getPlayerCollection(int playerId, String type) throws SQLException, IOException;

    public void overwriteCollectionContents(int playerId, String type, CardCollection collection, String reason) throws SQLException, IOException;

    void convertCollection(int playerId, String type) throws SQLException, IOException;

    List<DBDefs.Collection> getAllCollectionsForPlayer(int playerId);

    DBDefs.Collection getCollectionInfo(int playerId, String type);

    DBDefs.Collection getCollectionInfo(int collectionID);

    List<DBDefs.Collection> getCollectionInfosByType(String type);

    void addToCollectionContents(int playerId, String type, CardCollection collection, String source) throws SQLException, IOException;

    void removeFromCollectionContents(int playerId, String type, CardCollection collection, String source) throws SQLException, IOException;

    void updateCollectionInfo(int playerId, String type, Map<String, Object> extraInformation) throws SQLException, IOException;
}
