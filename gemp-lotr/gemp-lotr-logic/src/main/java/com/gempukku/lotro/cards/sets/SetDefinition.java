package com.gempukku.lotro.cards.sets;

import java.util.List;
import java.util.Set;

public interface SetDefinition {
    public String getSetName();

    public String getSetId();

    public boolean hasFlag(String flag);

    public List<String> getCardsOfRarity(String rarity);

    public List<String> getTengwarCards();

    public String getCardRarity(String cardId);

    public Set<String> getAllCards();
}
