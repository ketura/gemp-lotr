package com.gempukku.lotro.db;

import com.gempukku.lotro.collection.CollectionSerializer;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.MutableCardCollection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LeagueDAO {
    private DbAccess _dbAccess;
    private LotroCardBlueprintLibrary _library;

    private CollectionSerializer _serializer;

    private Set<League> _leagues = new HashSet<League>();

    public LeagueDAO(DbAccess dbAccess, LotroCardBlueprintLibrary library) {
        _dbAccess = dbAccess;
        _library = library;

        _serializer = new CollectionSerializer(_library);

        try {
            loadLeagues();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to load Leagues", e);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load Leagues", e);
        }
    }

    private void loadLeagues() throws SQLException, IOException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("select id, name, type, collection from league");
            try {
                ResultSet rs = statement.executeQuery();
                try {
                    while (rs.next()) {
                        int id = rs.getInt(1);
                        String name = rs.getString(2);
                        String type = rs.getString(3);
                        Blob baseCollection = rs.getBlob(4);
                        try {
                            final InputStream inputStream = baseCollection.getBinaryStream();
                            try {
                                MutableCardCollection collection = _serializer.deserializeCollection(inputStream);

                                _leagues.add(new League(id, name, type, collection));
                            } finally {
                                inputStream.close();
                            }
                        } finally {
                            baseCollection.free();
                        }
                    }
                } finally {
                    rs.close();
                }
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }

    public Set<League> getAllLeagues() {
        return Collections.unmodifiableSet(_leagues);
    }
}
