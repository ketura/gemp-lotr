package com.gempukku.lotro.draft2.builder;

import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.draft2.DraftChoiceDefinition;
import com.gempukku.lotro.draft2.SoloDraft;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.SortAndFilterCards;
import com.gempukku.lotro.game.formats.LotroFormatLibrary;
import com.gempukku.lotro.game.packs.SetDefinition;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class DraftChoiceBuilder {
    public static final int HIGH_ENOUGH_PRIME_NUMBER = 9497;
    private CollectionsManager _collectionsManager;
    private LotroCardBlueprintLibrary _cardLibrary;
    private LotroFormatLibrary _formatLibrary;
    private Map<String, SetDefinition> _rarities;
    private SortAndFilterCards _sortAndFilterCards;

    public DraftChoiceBuilder(CollectionsManager collectionsManager, LotroCardBlueprintLibrary cardLibrary,
                              LotroFormatLibrary formatLibrary, Map<String, SetDefinition> rarities) {
        _collectionsManager = collectionsManager;
        _cardLibrary = cardLibrary;
        _formatLibrary = formatLibrary;
        _rarities = rarities;
        _sortAndFilterCards = new SortAndFilterCards();
    }

    public DraftChoiceDefinition buildDraftChoiceDefinition(JSONObject choiceDefinition) {
        return constructDraftChoiceDefinition(choiceDefinition);
    }

    private DraftChoiceDefinition constructDraftChoiceDefinition(JSONObject choiceDefinition) {
        String choiceDefinitionType = (String) choiceDefinition.get("type");
        JSONObject data = (JSONObject) choiceDefinition.get("data");
        if (choiceDefinitionType.equals("singleCollectionPick"))
            return buildSingleCollectionPickDraftChoiceDefinition(data);
        else if (choiceDefinitionType.equals("weightedSwitch"))
            return buildWeightedSwitchDraftChoiceDefinition(data);
        else if (choiceDefinitionType.equals("multipleCardPick"))
            return buildMultipleCardPickDraftChoiceDefinition(data);
        else if (choiceDefinitionType.equals("randomSwitch"))
            return buildRandomSwitchDraftChoiceDefinition(data);
        else if (choiceDefinitionType.equals("filterPick"))
            return buildFilterPickDraftChoiceDefinition(data);
        else if (choiceDefinitionType.equals("draftPoolFilterPick"))
            return buildDraftPoolFilterPickDraftChoiceDefinition(data);
        else if (choiceDefinitionType.equals("draftPoolFilterPluck"))
            return buildDraftPoolFilterPluckDraftChoiceDefinition(data);
        else
            throw new RuntimeException("Unknown choiceDefinitionType: " + choiceDefinitionType);
    }

    private DraftChoiceDefinition buildFilterPickDraftChoiceDefinition(JSONObject data) {
        final int optionCount = ((Number) data.get("optionCount")).intValue();
        String filter = (String) data.get("filter");

        Iterable<CardCollection.Item> items = _collectionsManager.getDefaultCollection().getAll();

        final List<CardCollection.Item> possibleCards = _sortAndFilterCards.process(filter, items, _cardLibrary, _formatLibrary, _rarities);

        return new DraftChoiceDefinition() {
            @Override
            public Iterable<SoloDraft.DraftChoice> getDraftChoice(long seed, int stage, DefaultCardCollection draftPool) {
                final List<CardCollection.Item> cards = getCards(seed, stage);

                List<SoloDraft.DraftChoice> draftChoices = new ArrayList<SoloDraft.DraftChoice>(optionCount);
                for (int i = 0; i < Math.min(optionCount, possibleCards.size()); i++) {
                    final int finalI = i;
                    draftChoices.add(
                            new SoloDraft.DraftChoice() {
                                @Override
                                public String getChoiceId() {
                                    return cards.get(finalI).getBlueprintId();
                                }

                                @Override
                                public String getBlueprintId() {
                                    return cards.get(finalI).getBlueprintId();
                                }

                                @Override
                                public String getChoiceUrl() {
                                    return null;
                                }
                            });
                }
                return draftChoices;
            }

            @Override
            public CardCollection getCardsForChoiceId(String choiceId, long seed, int stage) {
                DefaultCardCollection cardCollection = new DefaultCardCollection();
                cardCollection.addItem(choiceId, 1);
                return cardCollection;
            }

            private List<CardCollection.Item> getCards(long seed, int stage) {
                Random rnd = getRandom(seed, stage);
                // Fixing some weird issue with Random
                float thisFixesRandomnessForSomeReason = rnd.nextInt();
                final List<CardCollection.Item> cards = new ArrayList<CardCollection.Item>(possibleCards);
                Collections.shuffle(cards, rnd);
                return cards;
            }
        };
    }

    private DraftChoiceDefinition buildSingleCollectionPickDraftChoiceDefinition(JSONObject data) {
        JSONArray switchResult = (JSONArray) data.get("possiblePicks");

        final Map<String, List<String>> cardsMap = new HashMap<String, List<String>>();
        final List<SoloDraft.DraftChoice> draftChoices = new ArrayList<SoloDraft.DraftChoice>();

        for (JSONObject pickDefinition : (Iterable<JSONObject>) switchResult) {
            final String choiceId = (String) pickDefinition.get("choiceId");
            final String url = (String) pickDefinition.get("url");
            JSONArray cards = (JSONArray) pickDefinition.get("cards");

            List<String> cardIds = new ArrayList<String>();
            for (String card : (Iterable<String>) cards)
                cardIds.add(card);

            draftChoices.add(
                    new SoloDraft.DraftChoice() {
                        @Override
                        public String getChoiceId() {
                            return choiceId;
                        }

                        @Override
                        public String getBlueprintId() {
                            return null;
                        }

                        @Override
                        public String getChoiceUrl() {
                            return url;
                        }
                    });
            cardsMap.put(choiceId, cardIds);
        }

        return new DraftChoiceDefinition() {
            @Override
            public Iterable<SoloDraft.DraftChoice> getDraftChoice(long seed, int stage, DefaultCardCollection draftPool) {
                return draftChoices;
            }

            @Override
            public CardCollection getCardsForChoiceId(String choiceId, long seed, int stage) {
                List<String> cardIds = cardsMap.get(choiceId);
                DefaultCardCollection cardCollection = new DefaultCardCollection();
                if (cardIds != null)
                    for (String cardId : cardIds)
                        cardCollection.addItem(cardId, 1);

                return cardCollection;
            }
        };
    }

    private DraftChoiceDefinition buildMultipleCardPickDraftChoiceDefinition(JSONObject data) {
        final int count = ((Number) data.get("count")).intValue();
        JSONArray availableCards = (JSONArray) data.get("availableCards");

        final List<String> cards = new ArrayList<String>();
        for (String availableCard : (Iterable<String>) availableCards)
            cards.add(availableCard);

        return new DraftChoiceDefinition() {
            @Override
            public Iterable<SoloDraft.DraftChoice> getDraftChoice(long seed, int stage, DefaultCardCollection draftPool) {
                final List<String> shuffledCards = getShuffledCards(seed, stage);

                List<SoloDraft.DraftChoice> draftableCards = new ArrayList<SoloDraft.DraftChoice>(count);
                for (int i = 0; i < count; i++) {
                    final int finalI = i;
                    draftableCards.add(
                            new SoloDraft.DraftChoice() {
                                @Override
                                public String getChoiceId() {
                                    return shuffledCards.get(finalI);
                                }

                                @Override
                                public String getBlueprintId() {
                                    return shuffledCards.get(finalI);
                                }

                                @Override
                                public String getChoiceUrl() {
                                    return null;
                                }
                            });
                }
                return draftableCards;
            }

            @Override
            public CardCollection getCardsForChoiceId(String choiceId, long seed, int stage) {
                List<String> shuffledCards = getShuffledCards(seed, stage);

                for (int i = 0; i < count; i++) {
                    if (shuffledCards.get(i).equals(choiceId)) {
                        DefaultCardCollection result = new DefaultCardCollection();
                        result.addItem(choiceId, 1);
                        return result;
                    }
                }

                return new DefaultCardCollection();
            }

            private List<String> getShuffledCards(long seed, int stage) {
                Random rnd = getRandom(seed, stage);
                // Fixing some weird issue with Random
                float thisFixesRandomnessForSomeReason = rnd.nextFloat();
                final List<String> shuffledCards = new ArrayList<String>(cards);
                Collections.shuffle(shuffledCards, rnd);
                return shuffledCards;
            }
        };
    }
    
    private DraftChoiceDefinition buildDraftPoolFilterPickDraftChoiceDefinition(JSONObject data) {
        final int optionCount = ((Number) data.get("optionCount")).intValue();
        String filter = (String) data.get("filter");

        return new DraftChoiceDefinition() {
            @Override
            public Iterable<SoloDraft.DraftChoice> getDraftChoice(long seed, int stage, DefaultCardCollection draftPool) {
                
                List<CardCollection.Item> possibleCards = _sortAndFilterCards.process(filter, draftPool.getAll(), _cardLibrary, _formatLibrary, _rarities);

                final List<CardCollection.Item> cards = getCards(seed, stage, possibleCards);

                List<SoloDraft.DraftChoice> draftChoices = new ArrayList<SoloDraft.DraftChoice>(optionCount);
                for (int i = 0; i < Math.min(optionCount, possibleCards.size()); i++) {
                    final int finalI = i;
                    draftChoices.add(
                            new SoloDraft.DraftChoice() {
                                @Override
                                public String getChoiceId() {
                                    return cards.get(finalI).getBlueprintId();
                                }

                                @Override
                                public String getBlueprintId() {
                                    return cards.get(finalI).getBlueprintId();
                                }

                                @Override
                                public String getChoiceUrl() {
                                    return null;
                                }
                            });
                }
                return draftChoices;
            }

            @Override
            public CardCollection getCardsForChoiceId(String choiceId, long seed, int stage) {
                DefaultCardCollection cardCollection = new DefaultCardCollection();
                cardCollection.addItem(choiceId, 1);
                return cardCollection;
            }

            private List<CardCollection.Item> getCards(long seed, int stage, List<CardCollection.Item> possibleCards) {
                Random rnd = getRandom(seed, stage);
                // Fixing some weird issue with Random
                float thisFixesRandomnessForSomeReason = rnd.nextInt();
                final List<CardCollection.Item> cards = possibleCards;
                Collections.shuffle(cards, rnd);
                return cards;
            }
        };
    }
    
    private DraftChoiceDefinition buildDraftPoolFilterPluckDraftChoiceDefinition(JSONObject data) {
        final int optionCount = ((Number) data.get("optionCount")).intValue();
        String filter = (String) data.get("filter");

        return new DraftChoiceDefinition() {
            @Override
            public Iterable<SoloDraft.DraftChoice> getDraftChoice(long seed, int stage, DefaultCardCollection draftPool) {
                List<CardCollection.Item> fullDraftPool = new ArrayList<CardCollection.Item>();
                for (CardCollection.Item item : draftPool.getAll())
                    for (int i = 0; i < draftPool.getItemCount(item.getBlueprintId()); i++)
                        fullDraftPool.add(item);

                List<CardCollection.Item> possibleCards = _sortAndFilterCards.process(filter, fullDraftPool, _cardLibrary, _formatLibrary, _rarities);

                final List<CardCollection.Item> cards = getCards(seed, stage, possibleCards);

                List<SoloDraft.DraftChoice> draftChoices = new ArrayList<SoloDraft.DraftChoice>(optionCount);
                for (int i = 0; i < Math.min(optionCount, possibleCards.size()); i++) {
                    draftPool.removeItem(cards.get(i).getBlueprintId(),1);
                    final int finalI = i;
                    draftChoices.add(
                            new SoloDraft.DraftChoice() {
                                @Override
                                public String getChoiceId() {
                                    return cards.get(finalI).getBlueprintId();
                                }

                                @Override
                                public String getBlueprintId() {
                                    return cards.get(finalI).getBlueprintId();
                                }

                                @Override
                                public String getChoiceUrl() {
                                    return null;
                                }
                            });
                }
                return draftChoices;
            }

            @Override
            public CardCollection getCardsForChoiceId(String choiceId, long seed, int stage) {
                DefaultCardCollection cardCollection = new DefaultCardCollection();
                cardCollection.addItem(choiceId, 1);
                return cardCollection;
            }

            private List<CardCollection.Item> getCards(long seed, int stage, List<CardCollection.Item> possibleCards) {
                Random rnd = getRandom(seed, stage);
                // Fixing some weird issue with Random
                float thisFixesRandomnessForSomeReason = rnd.nextInt();
                final List<CardCollection.Item> cards = possibleCards;
                Collections.shuffle(cards, rnd);
                return cards;
            }
        };
    }

    private DraftChoiceDefinition buildRandomSwitchDraftChoiceDefinition(JSONObject data) {
        JSONArray switchResult = (JSONArray) data.get("switchResult");

        final List<DraftChoiceDefinition> draftChoiceDefinitionList = new ArrayList<DraftChoiceDefinition>();
        for (JSONObject switchResultObject : (Iterable<JSONObject>) switchResult)
            draftChoiceDefinitionList.add(constructDraftChoiceDefinition(switchResultObject));

        return new DraftChoiceDefinition() {
            @Override
            public Iterable<SoloDraft.DraftChoice> getDraftChoice(long seed, int stage, DefaultCardCollection draftPool) {
                Random rnd = getRandom(seed, stage);
                // Fixing some weird issue with Random
                float thisFixesRandomnessForSomeReason = rnd.nextFloat();
                return draftChoiceDefinitionList.get(rnd.nextInt(draftChoiceDefinitionList.size())).getDraftChoice(seed, stage, draftPool);
            }

            @Override
            public CardCollection getCardsForChoiceId(String choiceId, long seed, int stage) {
                Random rnd = getRandom(seed, stage);
                // Fixing some weird issue with Random
                float thisFixesRandomnessForSomeReason = rnd.nextFloat();
                return draftChoiceDefinitionList.get(rnd.nextInt(draftChoiceDefinitionList.size())).getCardsForChoiceId(choiceId, seed, stage);
            }
        };
    }

    private DraftChoiceDefinition buildWeightedSwitchDraftChoiceDefinition(JSONObject data) {
        JSONArray switchResult = (JSONArray) data.get("switchResult");

        final Map<Float, DraftChoiceDefinition> draftChoiceDefinitionMap = new LinkedHashMap<Float, DraftChoiceDefinition>();
        float weightTotal = 0;
        for (JSONObject switchResultObject : (Iterable<JSONObject>) switchResult) {
            float weight = ((Number) switchResultObject.get("weight")).floatValue();
            weightTotal += weight;
            draftChoiceDefinitionMap.put(weightTotal, constructDraftChoiceDefinition(switchResultObject));
        }

        return new DraftChoiceDefinition() {
            @Override
            public Iterable<SoloDraft.DraftChoice> getDraftChoice(long seed, int stage, DefaultCardCollection draftPool) {
                Random rnd = getRandom(seed, stage);
                float result = rnd.nextFloat();
                for (Map.Entry<Float, DraftChoiceDefinition> weightEntry : draftChoiceDefinitionMap.entrySet()) {
                    if (result < weightEntry.getKey())
                        return weightEntry.getValue().getDraftChoice(seed, stage, draftPool);
                }

                return null;
            }

            @Override
            public CardCollection getCardsForChoiceId(String choiceId, long seed, int stage) {
                Random rnd = getRandom(seed, stage);
                float result = rnd.nextFloat();
                for (Map.Entry<Float, DraftChoiceDefinition> weightEntry : draftChoiceDefinitionMap.entrySet()) {
                    if (result < weightEntry.getKey())
                        return weightEntry.getValue().getCardsForChoiceId(choiceId, seed, stage);
                }

                return null;
            }
        };
    }

    private Random getRandom(long seed, int stage) {
        return new Random(seed + stage * HIGH_ENOUGH_PRIME_NUMBER);
    }
}
