package com.gempukku.lotro.draft2.builder;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import com.google.common.collect.Iterators;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class StartingPoolBuilder {
    public CardCollectionProducer buildCardCollectionProducer(JSONObject startingPool) {
        String cardCollectionProducerType = (String) startingPool.get("type");
        if (cardCollectionProducerType.equals("randomCardPool")) {
            return buildRandomCardPool((JSONObject) startingPool.get("data"));
        } else if (cardCollectionProducerType.equals("boosterDraftRun")) {
            return buildBoosterDraftRun((JSONObject) startingPool.get("data"));
        }
        throw new RuntimeException("Unknown cardCollectionProducer type: " + cardCollectionProducerType);
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

    private CardCollectionProducer buildBoosterDraftRun(JSONObject boosterDraftRun) {
        final int runLengthInput = ((Number) boosterDraftRun.get("runLength")).intValue();
        final JSONArray coreCards = (JSONArray) boosterDraftRun.get("coreCards");
        final JSONArray freePeoplesRuns = (JSONArray) boosterDraftRun.get("freePeoplesRuns");
        final JSONArray shadowRuns = (JSONArray) boosterDraftRun.get("shadowRuns");

        return new CardCollectionProducer() {
            @Override
            public CardCollection getCardCollection(long seed) {
                Random rnd = new Random(seed);
                List<String> freePeoplesRun = ((List<List<String>>) freePeoplesRuns).get(rnd.nextInt(freePeoplesRuns.size()));
                List<String> shadowRun = ((List<List<String>>) shadowRuns).get(rnd.nextInt(shadowRuns.size()));

                int freePeopleLength = freePeoplesRun.size();
                int shadowLength = shadowRun.size();

                int runLength;
                if (freePeopleLength > 0 && shadowLength > 0) {
                    runLength = Math.min(runLengthInput, Math.min(freePeopleLength, shadowLength));
                } else if (freePeopleLength > 0) {
                    runLength = Math.min(runLengthInput, freePeopleLength);
                } else {
                    runLength = Math.min(runLengthInput, shadowLength);
                }

                int freePeopleStart = rnd.nextInt(freePeopleLength);
                int shadowStart = rnd.nextInt(shadowLength);

                Iterator<String> freePeopleIterator = getCyclingIterator(freePeoplesRun, freePeopleStart, runLength);
                Iterator<String> shadowIterator = getCyclingIterator(shadowRun, shadowStart, runLength);

                final DefaultCardCollection draftCollection = new DefaultCardCollection();

                for (String coreCard : (List<String>) coreCards) {
                    draftCollection.addItem(coreCard, 1);
                }
                while (freePeopleIterator.hasNext()) {
                    draftCollection.addItem(freePeopleIterator.next(), 1);
                }
                while (shadowIterator.hasNext()) {
                    draftCollection.addItem(shadowIterator.next(), 1);
                }
                return draftCollection;
            }
        };
    }

    private static Iterator<String> getCyclingIterator(List<String> list, int start, int length) {
        Iterator<String> cycleListIterator = Iterators.cycle(list);
        Iterators.skip(cycleListIterator, start);
        return Iterators.limit(cycleListIterator, length);
    }
}
