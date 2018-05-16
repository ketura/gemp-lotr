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
        } else if (cardCollectionProducerType.equals("boosterDraftRun")) {
            return buildBoosterDraftRun((JSONObject) startingPool.get("data"));
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
	
    private CardCollectionProducer buildBoosterDraftRun(JSONObject boosterDraftRun) {
        final int runLengthInput = ((Number) boosterDraftRun.get("runLength")).intValue();
        final JSONArray coreCards = (JSONArray) boosterDraftRun.get("coreCards");
        JSONArray freePeoplesRuns = (JSONArray) boosterDraftRun.get("freePeoplesRuns");
        JSONArray shadowRuns = (JSONArray) boosterDraftRun.get("shadowRuns");

        final List<List<String>> freePeoplesRunList = new ArrayList<List<String>>();
        Iterator<JSONArray> freePeoplesIterator = freePeoplesRuns.iterator();
        while (freePeoplesIterator.hasNext()) {
            JSONArray cards = freePeoplesIterator.next();

            List<String> orderedCards = new ArrayList<String>();
            Iterator<String> cardIterator = cards.iterator();
            while (cardIterator.hasNext()) {
                orderedCards.add(cardIterator.next());
            }
            freePeoplesRunList.add(orderedCards);
        }

        final List<List<String>> shadowRunList = new ArrayList<List<String>>();
        Iterator<JSONArray> shadowIterator = shadowRuns.iterator();
        while (shadowIterator.hasNext()) {
            JSONArray cards = shadowIterator.next();

            List<String> orderedCards = new ArrayList<String>();
            Iterator<String> cardIterator = cards.iterator();
            while (cardIterator.hasNext()) {
                orderedCards.add(cardIterator.next());
            }
            shadowRunList.add(orderedCards);
        }

        return new CardCollectionProducer() {
            @Override
            public CardCollection getCardCollection(long seed) {
                Random rnd = new Random(seed);
                List<String> freePeoplesRun = freePeoplesRunList.get(rnd.nextInt(freePeoplesRunList.size()));
                List<String> shadowRun = shadowRunList.get(rnd.nextInt(shadowRunList.size()));
                int runLength = 0;
                if (freePeoplesRun.size() > 0 && shadowRun.size() > 0) {
                    runLength = Math.min(runLengthInput, Math.min(freePeoplesRun.size(), shadowRun.size()));
                } else if (freePeoplesRun.size() > 0) {
                    runLength = Math.min(runLengthInput, freePeoplesRun.size());
                } else {
                    runLength = Math.min(runLengthInput, shadowRun.size());
                }
                String[] draftRun = new String[runLength];
                if (freePeoplesRun.size() > 0) {
                    int runSeed = rnd.nextInt() % freePeoplesRun.size();
                    if (runSeed + runLength > freePeoplesRun.size()) {
                        int runFromStart = runSeed + runLength - freePeoplesRun.size();
                        int runAtEnd = runLength - runFromStart;
                        for (int i = 0; i < runAtEnd; i++) {
                            draftRun[i] = freePeoplesRun.get(runSeed+i);
                        }
                        for (int i = 0; i < runFromStart; i++) {
                            draftRun[runAtEnd + i] = freePeoplesRun.get(i);
                        }
                    } else {
                        for (int i = 0; i < runLength; i++) {
                            draftRun[i] = freePeoplesRun.get(runSeed+i);
                        }
                    }
                }
                if (shadowRun.size() > 0) {
                    int runSeed = rnd.nextInt() % shadowRun.size();
                    if (runSeed + runLength > shadowRun.size()) {
                        int runFromStart = runSeed + runLength - shadowRun.size();
                        int runAtEnd = runLength - runFromStart;
                        for (int i = 0; i < runAtEnd; i++) {
                            draftRun[i] = shadowRun.get(runSeed+i);
                        }
                        for (int i = 0; i < runFromStart; i++) {
                            draftRun[runAtEnd + i] = shadowRun.get(i);
                        }
                    } else {
                        for (int i = 0; i < runLength; i++) {
                            draftRun[i] = shadowRun.get(runSeed+i);
                        }
                    }
                }

                final DefaultCardCollection draftCollection = new DefaultCardCollection();
                Iterator<String> iterator = coreCards.iterator();
                while (iterator.hasNext()) {
                    draftCollection.addItem(iterator.next(), 1);
                }
                for (int i = 0; i < draftRun.length; i++) {
                    draftCollection.addItem(draftRun[i], 1);
                }
                return draftCollection;
            }
        };
    }
}