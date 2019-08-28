package com.gempukku.lotro.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SumCardCollection implements CardCollection {
    private List<CardCollection> _cardCollections;

    public SumCardCollection(List<CardCollection> cardCollections) {
        _cardCollections = cardCollections;
    }

    @Override
    public int getCurrency() {
        int sum = 0;
        for (CardCollection cardCollection : _cardCollections)
            sum += cardCollection.getCurrency();

        return sum;
    }

    @Override
    public Map<String, Object> getExtraInformation() {
        Map<String, Object> result = new HashMap<String, Object>();
        for (CardCollection cardCollection : _cardCollections) {
            result.putAll(cardCollection.getExtraInformation());
        }
        return result;
    }

    @Override
    public Iterable<Item> getAll() {
        Map<String, Item> sum = new HashMap<String, Item>();
        for (CardCollection cardCollection : _cardCollections) {
            Iterable<Item> inCollection = cardCollection.getAll();
            for (Item cardCount : inCollection) {
                String cardId = cardCount.getBlueprintId();
                Integer count = sum.get(cardId).getCount();
                if (count != null)
                    sum.put(cardId, Item.createItem(cardId, count + cardCount.getCount()));
                else
                    sum.put(cardId, Item.createItem(cardId, cardCount.getCount()));
            }
        }

        return sum.values();
    }

    @Override
    public int getItemCount(String blueprintId) {
        int sum = 0;
        for (CardCollection cardCollection : _cardCollections)
            sum += cardCollection.getItemCount(blueprintId);

        return sum;
    }
}
