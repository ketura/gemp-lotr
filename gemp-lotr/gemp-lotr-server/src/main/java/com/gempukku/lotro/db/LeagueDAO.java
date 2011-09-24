package com.gempukku.lotro.db;

import com.gempukku.lotro.collection.CollectionSerializer;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.MutableCardCollection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class LeagueDAO {
    private DbAccess _dbAccess;
    private LotroCardBlueprintLibrary _library;

    private CollectionSerializer _serializer;

    private int _dateLoaded;
    private Set<League> _activeLeagues = new HashSet<League>();

    public LeagueDAO(DbAccess dbAccess, LotroCardBlueprintLibrary library) {
        _dbAccess = dbAccess;
        _library = library;

        _serializer = new CollectionSerializer(_library);
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

                                _activeLeagues.add(new League(id, name, type, collection));
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

    private int getCurrentDate() {
        Calendar date = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        return date.get(Calendar.YEAR) * 10000 + (date.get(Calendar.MONTH) + 1) * 100 + date.get(Calendar.DAY_OF_MONTH);
    }

    private synchronized void ensureLoadedCurrentLeagues() {
        int currentDate = getCurrentDate();
        if (currentDate != _dateLoaded) {
            try {
                loadLeagues();
                _dateLoaded = currentDate;
            } catch (SQLException e) {
                throw new RuntimeException("Unable to load Leagues", e);
            } catch (IOException e) {
                throw new RuntimeException("Unable to load Leagues", e);
            }
        }
    }

    public Set<League> getActiveLeagues() {
        if (getCurrentDate() == _dateLoaded)
            return Collections.unmodifiableSet(_activeLeagues);
        else {
            ensureLoadedCurrentLeagues();
            return Collections.unmodifiableSet(_activeLeagues);
        }
    }
}
