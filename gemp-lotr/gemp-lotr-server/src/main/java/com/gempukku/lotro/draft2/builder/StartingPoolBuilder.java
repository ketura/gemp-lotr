package com.gempukku.lotro.draft2.builder;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class StartingPoolBuilder {
    public CardCollectionProducer buildCardCollectionProducer(JSONObject startingPool) {
        String cardCollectionProducerType = (String) startingPool.get("type");
        if (cardCollectionProducerType.equals("randomCardPool")) {
            return buildRandomCardPool((JSONObject) startingPool.get("data"));
        }
        throw new RuntimeException("Unknown cardCollectionProducer type: "+cardCollectionProducerType);
    }

    private CardCollectionProducer buildRandomCardPool(JSONObject randomCardPool) {
        JSONArray cardPools = (JSONArray) randomCardPool.get("randomResult");

        final List<CardCollection> cardCollections = new ArrayList<CardCollection>();
        Iterator<JSONArray> iterator = cardPools.iterator();
        while (iterator.hasNext()) {
            JSONArray cards = iterator.next();

            DefaultCardCollection cardCollection = new DefaultCardCollection();
            Iterator<String> cardIterator = cards.iterator();
            while (cardIterator.hasNext()) {
                cardCollection.addItem(cardIterator.next(), 1);
            }
            cardCollections.add(cardCollection);
        }

        return new CardCollectionProducer() {
            @Override
            public CardCollection getCardCollection(long seed) {
                Random rnd = new Random(seed);
                return cardCollections.get(rnd.nextInt(cardCollections.size()));
            }
        };
    }
}
