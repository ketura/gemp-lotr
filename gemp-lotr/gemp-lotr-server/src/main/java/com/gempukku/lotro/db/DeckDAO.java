package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.DeckVO;
import com.gempukku.lotro.db.vo.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DeckDAO {
    private DbAccess _dbAccess;

    private Map<Integer, Map<String, DeckVO>> _decks = new ConcurrentHashMap<Integer, Map<String, DeckVO>>();

    public DeckDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    public void clearCache() {
        _decks.clear();
    }

    public DeckVO getDeckForPlayer(Player player, String name) {
        Map<String, DeckVO> deckMap = getPlayerDecks(player);
        return deckMap.get(name);
    }

    public synchronized void saveDeckForPlayer(Player player, String name, DeckVO deck) {
        final Map<String, DeckVO> playerDecks = getPlayerDecks(player);
        boolean newDeck = !playerDecks.containsKey(name);
        storeDeckToDB(player.getId(), name, deck, newDeck);
        playerDecks.put(name, deck);
    }

    public synchronized void deleteDeckForPlayer(Player player, String name) {
        try {
            deleteDeckFromDB(player.getId(), name);
            Map<String, DeckVO> map = getPlayerDecks(player);
            map.remove(name);
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to delete player deck from DB", exp);
        }
    }

    public synchronized DeckVO renameDeck(Player player, String oldName, String newName) {
        DeckVO deck = getDeckForPlayer(player, oldName);
        if (deck == null)
            return null;
        saveDeckForPlayer(player, newName, deck);
        deleteDeckForPlayer(player, oldName);

        return deck;
    }

    public Set<String> getPlayerDeckNames(Player player) {
        return Collections.unmodifiableSet(getPlayerDecks(player).keySet());
    }

    private Map<String, DeckVO> getPlayerDecks(Player player) {
        Map<String, DeckVO> decksByName = _decks.get(player.getId());
        if (decksByName == null) {
            decksByName = loadPlayerDecks(player);
            _decks.put(player.getId(), decksByName);
        }
        return decksByName;
    }

    private Map<String, DeckVO> loadPlayerDecks(Player player) {
        try {
            return loadPlayerDecksFromDB(player.getId());
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to load player decks from DB", exp);
        }
    }

    private Map<String, DeckVO> loadPlayerDecksFromDB(int playerId) throws SQLException {
        Connection connection = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("select name, contents from deck where player_id=?");
            try {
                statement.setInt(1, playerId);
                ResultSet rs = statement.executeQuery();
                try {
                    Map<String, DeckVO> result = new HashMap<String, DeckVO>();
                    while (rs.next()) {
                        String name = rs.getString(1);
                        String contents = rs.getString(2);

                        result.put(name, buildDeckFromContents(contents));
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

    }

    private void storeDeckToDB(int playerId, String name, DeckVO deck, boolean newDeck) {
        StringBuilder sb = new StringBuilder();
        sb.append(deck.getRingBearer());
        sb.append(",").append(deck.getRing());
        appendList(sb, deck.getCards());

        String contents = sb.toString();
        try {
            if (newDeck)
                storeDeckInDB(playerId, name, contents);
            else
                updateDeckInDB(playerId, name, contents);
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to store player deck to DB", exp);
        }
    }

    private void appendList(StringBuilder sb, List<String> cards) {
        for (String card : cards)
            sb.append("," + card);
    }

    private DeckVO getDeckFromDB(int playerId, String name) {
        try {
            String contents = getDeckContentsFromDB(playerId, name);
            if (contents != null) {
                return buildDeckFromContents(contents);
            } else {
                return null;
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to get player deck from DB", exp);
        }
    }

    private DeckVO buildDeckFromContents(String contents) {
        List<String> cardsList = Arrays.asList(contents.split(","));
        String ringBearer = cardsList.get(0);
        String ring = cardsList.get(1);
        return new DeckVO(ringBearer, ring, cardsList.subList(2, cardsList.size()));
    }

    private void deleteDeckFromDB(int playerId, String name) throws SQLException {
        Connection connection = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("delete from deck where player_id=? and name=?");
            try {
                statement.setInt(1, playerId);
                statement.setString(2, name);
                statement.execute();
            } finally {
                statement.close();
            }
        } finally {
            connection.close();
        }
    }

    private void storeDeckInDB(int playerId, String name, String contents) throws SQLException {
        Connection connection = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("insert into deck (player_id, name, contents) values (?, ?, ?)");
            try {
                statement.setInt(1, playerId);
                statement.setString(2, name);
                statement.setString(3, contents);
                statement.execute();
            } finally {
                statement.close();
            }
        } finally {
            connection.close();
        }
    }

    private void updateDeckInDB(int playerId, String name, String contents) throws SQLException {
        Connection connection = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("update deck set contents=? where player_id=? and name=?");
            try {
                statement.setString(1, contents);
                statement.setInt(2, playerId);
                statement.setString(3, name);
                statement.execute();
            } finally {
                statement.close();
            }
        } finally {
            connection.close();
        }
    }

    private String getDeckContentsFromDB(int playerId, String name) throws SQLException {
        Connection connection = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("select contents from deck where player_id=? and name=?");
            try {
                statement.setInt(1, playerId);
                statement.setString(2, name);
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
