package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.Deck;
import com.gempukku.lotro.db.vo.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeckDAO {
    private DbAccess _dbAccess;

    private Map<Integer, Map<String, Deck>> _decks = new HashMap<Integer, Map<String, Deck>>();

    public DeckDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    public Deck getDeckForPlayer(Player player, String type) {
        Map<String, Deck> playerDecks = _decks.get(player.getId());
        if (playerDecks != null) {
            Deck deck = playerDecks.get(type);
            if (deck != null)
                return deck;
        }

        Deck deck = getDeckFromDB(player.getId(), type);
        if (deck != null) {
            Map<String, Deck> decksByType = _decks.get(player.getId());
            if (decksByType == null) {
                decksByType = new HashMap<String, Deck>();
                _decks.put(player.getId(), decksByType);
            }
            decksByType.put(type, deck);
            return deck;
        }
        return null;
    }

    public void setDeckForPlayer(Player player, String type, Deck deck) {
        storeDeckToDB(player.getId(), type, deck);
        Map<String, Deck> decksByType = _decks.get(player.getId());
        if (decksByType == null) {
            decksByType = new HashMap<String, Deck>();
            _decks.put(player.getId(), decksByType);
        }
        decksByType.put(type, deck);
    }

    private void storeDeckToDB(int playerId, String type, Deck deck) {
        StringBuilder sb = new StringBuilder();
        sb.append(deck.getRingBearer());
        sb.append(",").append(deck.getRing());
        appendList(sb, deck.getSites());
        appendList(sb, deck.getDeck());

        String contents = sb.toString();
        try {
            deleteDeckFromDB(playerId, type);
            storeDeckContentsToDB(playerId, type, contents);
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to store player deck to DB", exp);
        }
    }

    private void appendList(StringBuilder sb, List<String> cards) {
        for (String card : cards)
            sb.append("," + card);
    }

    private Deck getDeckFromDB(int playerId, String type) {
        try {
            String contents = getDeckContentsFromDB(playerId, type);
            if (contents != null) {
                return buildDeckFromContents(contents);
            } else {
                return null;
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to get player deck from DB", exp);
        }
    }

    private Deck buildDeckFromContents(String contents) {
        List<String> cardsList = Arrays.asList(contents.split(","));
        return new Deck(cardsList.get(0), cardsList.get(1), cardsList.subList(2, 11), cardsList.subList(11, cardsList.size()));
    }

    private void deleteDeckFromDB(int playerId, String type) throws SQLException {
        Connection connection = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("delete from deck where player_id=? and type=?");
            try {
                statement.setInt(1, playerId);
                statement.setString(2, type);
                statement.execute();
            } finally {
                statement.close();
            }
        } finally {
            connection.close();
        }
    }

    private void storeDeckContentsToDB(int playerId, String type, String contents) throws SQLException {
        Connection connection = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("insert into deck (player_id, type, contents) values (?, ?, ?)");
            try {
                statement.setInt(1, playerId);
                statement.setString(2, type);
                statement.setString(3, contents);
                statement.execute();
            } finally {
                statement.close();
            }
        } finally {
            connection.close();
        }
    }

    private String getDeckContentsFromDB(int playerId, String type) throws SQLException {
        Connection connection = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("select contents from deck where player_id=? and type=?");
            try {
                statement.setInt(1, playerId);
                statement.setString(2, type);
                ResultSet rs = statement.executeQuery();
                try {
                    if (rs.next()) {
                        return rs.getString(1);
                    } else {
                        return null;
                    }
                } finally {
                    rs.close();
                }
            } finally {
                statement.close();
            }
        } finally {
            connection.close();
        }
    }
}
