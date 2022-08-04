package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.League;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface LeagueDAO {
    public void addLeague(int cost, String name, String type, String clazz, String parameters, int start, int endTime) throws SQLException;

    public List<League> loadActiveLeagues(int currentTime) throws SQLException;

    public void setStatus(League league, int newStatus);
}
