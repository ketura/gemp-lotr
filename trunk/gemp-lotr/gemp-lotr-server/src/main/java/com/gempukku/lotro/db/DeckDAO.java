package com.gempukku.lotro.db;

import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.Set;

public interface DeckDAO {
    public void clearCache();

    public LotroDeck getDeckForPlayer(Player player, String name);

    public void saveDeckForPlayer(Player player, String name, LotroDeck deck);

    public void deleteDeckForPlayer(Player player, String name);

    public LotroDeck renameDeck(Player player, String oldName, String newName);

    public Set<String> getPlayerDeckNames(Player player);

    public LotroDeck buildDeckFromContents(String deckName, String contents);
}
