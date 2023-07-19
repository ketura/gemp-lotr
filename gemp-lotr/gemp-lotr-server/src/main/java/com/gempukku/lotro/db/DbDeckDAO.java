package com.gempukku.lotro.db;

import com.gempukku.lotro.cards.CardNotFoundException;
import com.gempukku.lotro.cards.LotroCardBlueprint;
import com.gempukku.lotro.cards.CardBlueprintLibrary;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.cards.lotronly.LotroDeck;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DbDeckDAO implements DeckDAO {
    private final DbAccess _dbAccess;
    private final CardBlueprintLibrary _library;

    public DbDeckDAO(DbAccess dbAccess, CardBlueprintLibrary library) {
        _dbAccess = dbAccess;
        _library = library;
    }

    public synchronized LotroDeck getDeckForPlayer(Player player, String name) {
        return getPlayerDeck(player.getId(), name);
    }

    public synchronized void saveDeckForPlayer(Player player, String name, String target_format, String notes, LotroDeck deck) {
        boolean newDeck = getPlayerDeck(player.getId(), name) == null;
        storeDeckToDB(player.getId(), name, target_format, notes, deck, newDeck);
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
        saveDeckForPlayer(player, newName, deck.getTargetFormat(), deck.getNotes(), deck);
        deleteDeckForPlayer(player, oldName);

        return deck;
    }

    public synchronized Set<Map.Entry<String, String>> getPlayerDeckNames(Player player) {
        try {
            try (Connection connection = _dbAccess.getDataSource().getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("select name, target_format from deck where player_id=?")) {
                    statement.setInt(1, player.getId());
                    try (ResultSet rs = statement.executeQuery()) {
                        Set<Map.Entry<String, String>> result = new HashSet<>();

                        while (rs.next()) {
                            String deckName = rs.getString(1);
                            String targetFormat = rs.getString(2);
                            result.add(new AbstractMap.SimpleEntry<>(targetFormat, deckName));
                        }

                        return result;
                    }
                }
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to load player decks from DB", exp);
        }
    }

    private LotroDeck getPlayerDeck(int playerId, String name) {
        try {
            try (Connection connection = _dbAccess.getDataSource().getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("select contents, target_format, notes from deck where player_id=? and name=?")) {
                    statement.setInt(1, playerId);
                    statement.setString(2, name);
                    try (ResultSet rs = statement.executeQuery()) {
                        if (rs.next())
                            return buildLotroDeckFromContents(name, rs.getString(1), rs.getString(2), rs.getString(3));

                        return null;
                    }
                }
            }

        } catch (SQLException exp) {
            throw new RuntimeException("Unable to load player decks from DB", exp);
        }
    }

    private void storeDeckToDB(int playerId, String name, String target_format, String notes, LotroDeck deck, boolean newDeck) {
        String contents = deck.buildContentsFromDeck();
        try {
            if (newDeck)
                storeDeckInDB(playerId, name, target_format, notes, contents);
            else
                updateDeckInDB(playerId, name, target_format, notes, contents);
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to store player deck to DB", exp);
        }
    }

    public synchronized LotroDeck buildLotroDeckFromContents(String deckName, String contents, String target_format, String notes) {
        if (contents.contains("|")) {
            return new LotroDeck(deckName, contents, target_format, notes);
        } else {
            // Old format
            List<String> cardsList = Arrays.asList(contents.split(","));
            String ringBearer = cardsList.get(0);
            String ring = cardsList.get(1);
            final LotroDeck lotroDeck = new LotroDeck(deckName);
            lotroDeck.setTargetFormat(target_format);
            lotroDeck.setNotes(notes);
            if (ringBearer.length() > 0)
                lotroDeck.setRingBearer(ringBearer);
            if (ring.length() > 0)
                lotroDeck.setRing(ring);
            for (String blueprintId : cardsList.subList(2, cardsList.size())) {
                final LotroCardBlueprint cardBlueprint;
                try {
                    cardBlueprint = _library.getLotroCardBlueprint(blueprintId);
                    if (cardBlueprint.getCardType() == CardType.SITE)
                        lotroDeck.addSite(blueprintId);
                    else
                        lotroDeck.addCard(blueprintId);
                } catch (CardNotFoundException e) {
                    // Ignore the card
                }
            }

            return lotroDeck;
        }
    }

    private void deleteDeckFromDB(int playerId, String name) throws SQLException {
        try (Connection connection = _dbAccess.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("delete from deck where player_id=? and name=?")) {
                statement.setInt(1, playerId);
                statement.setString(2, name);
                statement.execute();
            }
        }
    }

    private void storeDeckInDB(int playerId, String name, String target_format, String notes, String contents) throws SQLException {
        try (Connection connection = _dbAccess.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into deck (player_id, name, target_format, notes, contents) values (?, ?, ?, ?, ?)")) {
                statement.setInt(1, playerId);
                statement.setString(2, name);
                statement.setString(3, target_format);
                statement.setString(4, notes);
                statement.setString(5, contents);
                statement.execute();
            }
        }
    }

    private void updateDeckInDB(int playerId, String name, String target_format, String notes, String contents) throws SQLException {
        try (Connection connection = _dbAccess.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("update deck set contents=?, target_format=?, notes=? where player_id=? and name=?")) {
                statement.setString(1, contents);
                statement.setString(2, target_format);
                statement.setString(3, notes);
                statement.setInt(4, playerId);
                statement.setString(5, name);
                statement.execute();
            }
        }
    }
}
