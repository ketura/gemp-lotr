package com.gempukku.lotro.cards.packs;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DefaultSetRarity implements SetRarity {
    private List<String> _tengwarCards;
    private Map<String, List<String>> _rarityList;
    private Map<String, String> _cardsRarity;

    public DefaultSetRarity(List<String> tengwarCards, Map<String, List<String>> rarityList, Map<String, String> cardsRarity) {
        _tengwarCards = tengwarCards;
        _rarityList = rarityList;
        _cardsRarity = cardsRarity;
    }

    @Override
    public List<String> getCardsOfRarity(String rarity) {
        final List<String> list = _rarityList.get(rarity);
        if (list == null)
            return Collections.emptyList();
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<String> getTengwarCards() {
        if (_tengwarCards == null)
            return Collections.emptyList();
        return Collections.unmodifiableList(_tengwarCards);
    }

    @Override
    public String getCardRarity(String cardId) {
        return _cardsRarity.get(cardId);
    }
}
