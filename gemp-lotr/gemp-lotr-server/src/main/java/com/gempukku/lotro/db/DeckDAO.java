package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.Player;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DeckDAO {
    private DbAccess _dbAccess;

    private Map<Integer, Map<String, LotroDeck>> _decks = new ConcurrentHashMap<Integer, Map<String, LotroDeck>>();

    public DeckDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    public LotroDeck getDeckForPlayer(Player player, String type) {
        Map<String, LotroDeck> playerDecks = _decks.get(player.getId());
        if (playerDecks != null) {
            LotroDeck deck = playerDecks.get(type);
            if (deck != null)
                return deck;
        }

        LotroDeck deck = getDeckFromDB(player.getId(), type);
        if (deck != null) {
            Map<String, LotroDeck> decksByType = _decks.get(player.getId());
            if (decksByType == null) {
                decksByType = new ConcurrentHashMap<String, LotroDeck>();
                _decks.put(player.getId(), decksByType);
            }
            decksByType.put(type, deck);
            return deck;
        }
        return null;
    }

    public void setDeckForPlayer(Player player, String type, LotroDeck deck) {
        storeDeckToDB(player.getId(), type, deck);
        Map<String, LotroDeck> decksByType = _decks.get(player.getId());
        if (decksByType == null) {
            decksByType = new ConcurrentHashMap<String, LotroDeck>();
            _decks.put(player.getId(), decksByType);
        }
        decksByType.put(type, deck);
    }

    private void storeDeckToDB(int playerId, String type, LotroDeck deck) {
        StringBuilder sb = new StringBuilder();
        sb.append(deck.getRingBearer());
        sb.append(",").append(deck.getRing());
        appendList(sb, deck.getSites());
        appendList(sb, deck.getAdventureCards());

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

    private LotroDeck getDeckFromDB(int playerId, String type) {
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

    private LotroDeck buildDeckFromContents(String contents) {
        List<String> cardsList = Arrays.asList(contents.split(","));
        LotroDeck deck = new LotroDeck();
        deck.setRingBearer(cardsList.get(0));
        deck.setRing(cardsList.get(1));
        for (int i = 2; i < 11; i++)
            deck.addSite(cardsList.get(i));
        for (int i = 11; i < cardsList.size(); i++)
            deck.addCard(cardsList.get(i));
        return deck;
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
