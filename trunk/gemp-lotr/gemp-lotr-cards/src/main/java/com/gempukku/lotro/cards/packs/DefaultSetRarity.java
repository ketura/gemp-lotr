package com.gempukku.lotro.cards.packs;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DefaultSetRarity implements SetRarity {
    private List<String> _tengwarCards;
    private Map<String, List<String>> _rarityList;

    public DefaultSetRarity(List<String> tengwarCards, Map<String, List<String>> rarityList) {
        _tengwarCards = tengwarCards;
        _rarityList = rarityList;
    }

    @Override
    public List<String> getCardsOfRarity(String rarity) {
        return Collections.unmodifiableList(_rarityList.get(rarity));
    }

    @Override
    public List<String> getTengwarCards() {
        return Collections.unmodifiableList(_tengwarCards);
    }
}
