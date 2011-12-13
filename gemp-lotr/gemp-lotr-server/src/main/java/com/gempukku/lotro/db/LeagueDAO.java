package com.gempukku.lotro.db;

import com.gempukku.lotro.collection.CollectionSerializer;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.MutableCardCollection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class LeagueDAO {
    private DbAccess _dbAccess;

    private CollectionSerializer _serializer;

    private int _dateLoaded;
    private Set<League> _activeLeagues = new HashSet<League>();

    public LeagueDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;

        _serializer = new CollectionSerializer();
    }

    public void clearCache() {
        _dateLoaded = 0;
        _activeLeagues.clear();
    }

    public void addLeague(String name, String type, CardCollection collection, int startTime, int endTime) throws SQLException, IOException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("insert into league (name, type, collection, start, end) values (?, ?, ?, ?, ?)");
            try {
                statement.setString(1, name);
                statement.setString(2, type);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                _serializer.serializeCollection(collection, outputStream);
                statement.setBlob(3, new ByteArrayInputStream(outputStream.toByteArray()));
                statement.setInt(4, startTime);
                statement.setInt(5, endTime);
                statement.execute();
                _dateLoaded = 0;
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }

    public void setBaseCollectionForLeague(League league, CardCollection collection) throws SQLException, IOException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("update league set collection=? where type=?");
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                _serializer.serializeCollection(collection, outputStream);
                statement.setBlob(1, new ByteArrayInputStream(outputStream.toByteArray()));
                statement.setString(2, league.getType());
                statement.execute();
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }

    private void loadActiveLeagues(int currentTime) throws SQLException, IOException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("select id, name, type, collection, start, end from league where start<=? and end>=?");
            try {
                statement.setInt(1, currentTime);
                statement.setInt(2, currentTime);
                ResultSet rs = statement.executeQuery();
                try {
                    while (rs.next()) {
                        int id = rs.getInt(1);
                        String name = rs.getString(2);
                        String type = rs.getString(3);
                        int start = rs.getInt(5);
                        int end = rs.getInt(6);
                        Blob baseCollection = rs.getBlob(4);
                        try {
                            final InputStream inputStream = baseCollection.getBinaryStream();
                            try {
                                MutableCardCollection collection = _serializer.deserializeCollection(inputStream);

                                _activeLeagues.add(new League(id, type, name, collection, start, end));
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
                _activeLeagues.clear();
                loadActiveLeagues(currentDate);
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
