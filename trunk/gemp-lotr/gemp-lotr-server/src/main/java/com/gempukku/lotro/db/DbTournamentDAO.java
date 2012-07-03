package com.gempukku.lotro.db;

import com.gempukku.lotro.tournament.TournamentDAO;
import com.gempukku.lotro.tournament.TournamentInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbTournamentDAO implements TournamentDAO {
    private DbAccess _dbAccess;

    public DbTournamentDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    @Override
    public void addTournament(String tournamentId, String className, String parameters, Date start) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("insert into tournament (tournament_id, class, parameters, start) values (?, ?, ?, ?)");
                try {
                    statement.setString(1, tournamentId);
                    statement.setString(2, className);
                    statement.setString(3, parameters);
                    statement.setLong(4, start.getTime());
                    statement.execute();
                } finally {
                    statement.close();
                }
            } finally {
                conn.close();
            }
        } catch (SQLException exp) {
            throw new RuntimeException(exp);
        }
    }

    @Override
    public TournamentInfo getTournamentById(String tournamentId) {
        try {
            Connection connection = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = connection.prepareStatement("select class, parameters, start from tournament where tournament_id=?");
                try {
                    statement.setString(1, tournamentId);
                    ResultSet rs = statement.executeQuery();
                    try {
                        if (rs.next())
                            return new TournamentInfo(tournamentId, rs.getString(1), rs.getString(2), new Date(rs.getLong(3)));
                        else
                            return null;
                    } finally {
                        rs.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException exp) {
            throw new RuntimeException(exp);
        }
    }

    @Override
    public List<TournamentInfo> getUnfinishedTournaments() {
        try {
            Connection connection = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = connection.prepareStatement("select tournament_id, class, parameters, start from tournament where finished=false");
                try {
                    ResultSet rs = statement.executeQuery();
                    try {
                        List<TournamentInfo> result = new ArrayList<TournamentInfo>();
                        while (rs.next())
                            result.add(new TournamentInfo(rs.getString(1), rs.getString(2), rs.getString(3), new Date(rs.getLong(4))));
                        return result;
                    } finally {
                        rs.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException exp) {
            throw new RuntimeException(exp);
        }
    }

    @Override
    public List<TournamentInfo> getFinishedTournamentsSince(long time) {
        try {
            Connection connection = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = connection.prepareStatement("select tournament_id, class, parameters, start from tournament where finished=true and start>?");
                try {
                    statement.setLong(1, time);
                    ResultSet rs = statement.executeQuery();
                    try {
                        List<TournamentInfo> result = new ArrayList<TournamentInfo>();
                        while (rs.next())
                            result.add(new TournamentInfo(rs.getString(1), rs.getString(2), rs.getString(3), new Date(rs.getLong(4))));
                        return result;
                    } finally {
                        rs.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException exp) {
            throw new RuntimeException(exp);
        }
    }

    @Override
    public void markTournamentFinished(String tournamentId) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("update tournament set finished=true where tournament_id=?");
                try {
                    statement.setString(1, tournamentId);
                    statement.executeUpdate();
                } finally {
                    statement.close();
                }
            } finally {
                conn.close();
            }
        } catch (SQLException exp) {
            throw new RuntimeException(exp);
        }
    }
}
