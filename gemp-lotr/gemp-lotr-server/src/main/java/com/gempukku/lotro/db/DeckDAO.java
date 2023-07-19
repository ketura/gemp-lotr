package com.gempukku.lotro.db;

import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.cards.lotronly.LotroDeck;

import java.util.Map;
import java.util.Set;

public interface DeckDAO {
    LotroDeck getDeckForPlayer(Player player, String name);

    void saveDeckForPlayer(Player player, String name, String target_format, String notes, LotroDeck deck);

    void deleteDeckForPlayer(Player player, String name);

    LotroDeck renameDeck(Player player, String oldName, String newName);

    Set<Map.Entry<String, String>> getPlayerDeckNames(Player player);

    LotroDeck buildLotroDeckFromContents(String deckName, String contents, String target_format, String notes);
}
