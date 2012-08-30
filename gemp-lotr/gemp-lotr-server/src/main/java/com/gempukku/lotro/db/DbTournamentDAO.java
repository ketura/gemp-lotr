package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.tournament.Tournament;
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
    public void addTournament(String tournamentId, String draftType, String tournamentName, String format, CollectionType collectionType, Tournament.Stage stage, String pairingMechanism, Date start) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("insert into tournament (tournament_id, draft_type, name, format, collection, stage, pairing, start, round) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                try {
                    statement.setString(1, tournamentId);
                    statement.setString(2, draftType);
                    statement.setString(3, tournamentName);
                    statement.setString(4, format);
                    statement.setString(5, collectionType.getCode()+":"+collectionType.getFullName());
                    statement.setString(6, stage.name());
                    statement.setString(7, pairingMechanism);
                    statement.setLong(8, start.getTime());
                    statement.setInt(9, 0);
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
                PreparedStatement statement = connection.prepareStatement("select draft_type, name, format, collection, stage, pairing, round from tournament where tournament_id=?");
                try {
                    statement.setString(1, tournamentId);
                    ResultSet rs = statement.executeQuery();
                    try {
                        if (rs.next()) {
                            String[] collectionTypeStr = rs.getString(4).split(":", 2);
                            return new TournamentInfo(
                                    tournamentId, rs.getString(1), rs.getString(2), rs.getString(3),
                                    new CollectionType(collectionTypeStr[0], collectionTypeStr[1]), Tournament.Stage.valueOf(rs.getString(5)),
                                    rs.getString(6), rs.getInt(7));
                        } else
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
                PreparedStatement statement = connection.prepareStatement("select tournament_id, draft_type, name, format, collection, stage, pairing, round from tournament where stage <> '"+ Tournament.Stage.FINISHED.name()+"'");
                try {
                    ResultSet rs = statement.executeQuery();
                    try {
                        List<TournamentInfo> result = new ArrayList<TournamentInfo>();
                        while (rs.next()) {
                            String[] collectionTypeStr = rs.getString(5).split(":", 2);
                            result.add(new TournamentInfo(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                                    new CollectionType(collectionTypeStr[0], collectionTypeStr[1]), Tournament.Stage.valueOf(rs.getString(6)),
                                    rs.getString(7), rs.getInt(8)));
                        }
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
                PreparedStatement statement = connection.prepareStatement("select tournament_id, draft_type, name, format, collection, stage, pairing, round from tournament where stage = '"+ Tournament.Stage.FINISHED.name()+"' and start>?");
                try {
                    statement.setLong(1, time);
                    ResultSet rs = statement.executeQuery();
                    try {
                        List<TournamentInfo> result = new ArrayList<TournamentInfo>();
                         while (rs.next()) {
                            String[] collectionTypeStr = rs.getString(5).split(":", 2);
                            result.add(new TournamentInfo(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                                    new CollectionType(collectionTypeStr[0], collectionTypeStr[1]), Tournament.Stage.valueOf(rs.getString(6)),
                                    rs.getString(7), rs.getInt(8)));
                        }
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
    public void updateTournamentStage(String tournamentId, Tournament.Stage stage) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("update tournament set stage=? where tournament_id=?");
                try {
                    statement.setString(1, stage.name());
                    statement.setString(2, tournamentId);
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

    @Override
    public void updateTournamentRound(String tournamentId, int round) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("update tournament set round=? where tournament_id=?");
                try {
                    statement.setInt(1, round);
                    statement.setString(2, tournamentId);
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
