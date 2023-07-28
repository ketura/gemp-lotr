package com.gempukku.lotro.db;

import com.gempukku.lotro.game.User;
import com.gempukku.lotro.cards.lotronly.LotroDeck;

import java.util.Map;
import java.util.Set;

public interface DeckDAO {
    LotroDeck getDeckForPlayer(User player, String name);

    void saveDeckForPlayer(User player, String name, String target_format, String notes, LotroDeck deck);

    void deleteDeckForPlayer(User player, String name);

    LotroDeck renameDeck(User player, String oldName, String newName);

    Set<Map.Entry<String, String>> getPlayerDeckNames(User player);

    LotroDeck buildLotroDeckFromContents(String deckName, String contents, String target_format, String notes);
}
