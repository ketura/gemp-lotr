package com.gempukku.lotro.cards.packs;

import java.util.List;
import java.util.Set;

public interface SetRarity {
    public List<String> getCardsOfRarity(String rarity);

    public List<String> getTengwarCards();

    public String getCardRarity(String cardId);

    public Set<String> getAllCards();
}
