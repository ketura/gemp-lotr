package com.gempukku.lotro.draft2.builder;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import com.google.common.collect.Iterables;
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
                List<String> freePeoplesRun = (List<String>) freePeoplesRuns.get(rnd.nextInt(freePeoplesRuns.size()));
                List<String> shadowRun = (List<String>) shadowRuns.get(rnd.nextInt(shadowRuns.size()));

                int freePeopleLength = freePeoplesRun.size();
                int freePeopleStart = rnd.nextInt(freePeopleLength);

                int shadowLength = shadowRun.size();
                int shadowStart = rnd.nextInt(shadowLength);

                int runLength;
                if (freePeopleLength > 0 && shadowLength > 0) {
                    runLength = Math.min(runLengthInput, Math.min(freePeopleLength, shadowLength));
                } else if (freePeopleLength > 0) {
                    runLength = Math.min(runLengthInput, freePeopleLength);
                } else {
                    runLength = Math.min(runLengthInput, shadowLength);
                }

                Iterable<String> freePeopleIterable = getCyclingIterable(freePeoplesRun, freePeopleStart, runLength);
                Iterable<String> shadowIterable = getCyclingIterable(shadowRun, shadowStart, runLength);

                final DefaultCardCollection startingCollection = new DefaultCardCollection();

                for (String card : Iterables.concat((List<String>) coreCards, freePeopleIterable, shadowIterable))
                    startingCollection.addItem(card, 1);

                return startingCollection;
            }
        };
    }

    private static Iterable<String> getCyclingIterable(List<String> list, int start, int length) {
        return Iterables.limit(Iterables.skip(Iterables.cycle(list), start), length);
    }
}
