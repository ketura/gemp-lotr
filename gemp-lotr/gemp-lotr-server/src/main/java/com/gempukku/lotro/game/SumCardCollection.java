package com.gempukku.lotro.game;

import org.apache.commons.collections.map.HashedMap;

import java.util.*;

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
    public Map<String, Item> getAll() {
        Map<String, Item> sum = new HashMap<String, Item>();
        for (CardCollection cardCollection : _cardCollections) {
            Map<String, Item> inCollection = cardCollection.getAll();
            for (Map.Entry<String, Item> cardCount : inCollection.entrySet()) {
                String cardId = cardCount.getKey();
                Integer count = sum.get(cardId).getCount();
                if (count != null)
                    sum.put(cardId, Item.createItem(cardId, count + cardCount.getValue().getCount()));
                else
                    sum.put(cardId, Item.createItem(cardId, cardCount.getValue().getCount()));
            }
        }

        return sum;
    }

    @Override
    public int getItemCount(String blueprintId) {
        int sum = 0;
        for (CardCollection cardCollection : _cardCollections)
            sum += cardCollection.getItemCount(blueprintId);

        return sum;
    }

    @Override
    public Set<BasicCardItem> getAllCardsInCollection() {
        Set<BasicCardItem> result = new HashSet<BasicCardItem>();
        for (CardCollection cardCollection : _cardCollections)
            result.addAll(cardCollection.getAllCardsInCollection());
        return result;
    }
}
