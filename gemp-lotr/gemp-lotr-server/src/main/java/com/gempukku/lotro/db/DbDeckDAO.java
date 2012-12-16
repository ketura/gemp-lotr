package com.gempukku.lotro.db;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DbDeckDAO implements DeckDAO {
    private DbAccess _dbAccess;
    private LotroCardBlueprintLibrary _library;

    public DbDeckDAO(DbAccess dbAccess, LotroCardBlueprintLibrary library) {
        _dbAccess = dbAccess;
        _library = library;
    }

    public synchronized LotroDeck getDeckForPlayer(Player player, String name) {
        return getPlayerDeck(player.getId(), name);
    }

    public synchronized void saveDeckForPlayer(Player player, String name, LotroDeck deck) {
        boolean newDeck = getPlayerDeck(player.getId(), name) == null;
        storeDeckToDB(player.getId(), name, deck, newDeck);
    }

    public synchronized void deleteDeckForPlayer(Player player, String name) {
        try {
            deleteDeckFromDB(player.getId(), name);
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to delete player deck from DB", exp);
        }
    }

    public synchronized LotroDeck renameDeck(Player player, String oldName, String newName) {
        LotroDeck deck = getDeckForPlayer(player, oldName);
        if (deck == null)
            return null;
        saveDeckForPlayer(player, newName, deck);
        deleteDeckForPlayer(player, oldName);

        return deck;
    }

    public synchronized Set<String> getPlayerDeckNames(Player player) {
        try {
            Connection connection = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = connection.prepareStatement("select name from deck where player_id=?");
                try {
                    statement.setInt(1, player.getId());
                    ResultSet rs = statement.executeQuery();
                    try {
                        Set<String> result = new HashSet<String>();

                        while (rs.next())
                            result.add(rs.getString(1));

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
            throw new RuntimeException("Unable to load player decks from DB", exp);
        }
    }

    private LotroDeck getPlayerDeck(int playerId, String name) {
        try {
            Connection connection = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = connection.prepareStatement("select contents from deck where player_id=? and name=?");
                try {
                    statement.setInt(1, playerId);
                    statement.setString(2, name);
                    ResultSet rs = statement.executeQuery();
                    try {
                        if (rs.next())
                            return buildDeckFromContents(name, rs.getString(1));

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
            throw new RuntimeException("Unable to load player decks from DB", exp);
        }
    }

    private void storeDeckToDB(int playerId, String name, LotroDeck deck, boolean newDeck) {
        String contents = DeckSerialization.buildContentsFromDeck(deck);
        try {
            if (newDeck)
                storeDeckInDB(playerId, name, contents);
            else
                updateDeckInDB(playerId, name, contents);
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to store player deck to DB", exp);
        }
    }

    public synchronized LotroDeck buildDeckFromContents(String deckName, String contents) {
        if (contents.contains("|")) {
            return DeckSerialization.buildDeckFromContents(deckName, contents);
        } else {
            // Old format
            List<String> cardsList = Arrays.asList(contents.split(","));
            String ringBearer = cardsList.get(0);
            String ring = cardsList.get(1);
            final LotroDeck lotroDeck = new LotroDeck(deckName);
            if (ringBearer.length() > 0)
                lotroDeck.setRingBearer(ringBearer);
            if (ring.length() > 0)
                lotroDeck.setRing(ring);
            for (String blueprintId : cardsList.subList(2, cardsList.size())) {
                if (_library.getLotroCardBlueprint(blueprintId).getCardType() == CardType.SITE)
                    lotroDeck.addSite(blueprintId);
                else
                    lotroDeck.addCard(blueprintId);
            }

            return lotroDeck;
        }
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
}
