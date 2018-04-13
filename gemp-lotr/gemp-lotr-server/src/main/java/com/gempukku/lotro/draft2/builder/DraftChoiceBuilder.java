package com.gempukku.lotro.draft2.builder;

import com.gempukku.lotro.draft2.DraftChoiceDefinition;
import com.gempukku.lotro.draft2.SoloDraft;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class DraftChoiceBuilder {
    public DraftChoiceDefinition buildDraftChoiceDefinition(JSONObject choiceDefinition) {
        return constructDraftChoiceDefinition(choiceDefinition);
    }

    private DraftChoiceDefinition constructDraftChoiceDefinition(JSONObject choiceDefinition) {
        String choiceDefinitionType = (String) choiceDefinition.get("type");
        JSONObject data = (JSONObject) choiceDefinition.get("data");
        if (choiceDefinitionType.equals("singleCollectionPick"))
            return buildSingleCollectionPickDraftChoiceDefinition(data);
        else if (choiceDefinitionType.equals("multipleCardPick"))
            return buildMultipleCardPickDraftChoiceDefinition(data);
        else if (choiceDefinitionType.equals("randomSwitch"))
            return buildRandomSwitchDraftChoiceDeifinition(data);
        else
            throw new RuntimeException("Unknown choiceDefinitionType: " + choiceDefinitionType);
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
            public Iterable<SoloDraft.DraftChoice> getDraftChoice(long seed, int stage) {
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
        final long selectionSeed = ((Number) data.get("seed")).longValue();
        final int count = ((Number) data.get("count")).intValue();
        JSONArray availableCards = (JSONArray) data.get("availableCards");

        final List<String> cards = new ArrayList<String>();
        for (String availableCard : (Iterable<String>) availableCards)
            cards.add(availableCard);

        return new DraftChoiceDefinition() {
            @Override
            public Iterable<SoloDraft.DraftChoice> getDraftChoice(long seed, int stage) {
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
                Random rnd = new Random(seed + selectionSeed + stage);
                final List<String> shuffledCards = new ArrayList<String>(cards);
                Collections.shuffle(shuffledCards, rnd);
                return shuffledCards;
            }
        };
    }

    private DraftChoiceDefinition buildRandomSwitchDraftChoiceDeifinition(JSONObject data) {
        final long selectionSeed = ((Number) data.get("seed")).longValue();
        JSONArray switchResult = (JSONArray) data.get("switchResult");

        final List<DraftChoiceDefinition> draftChoiceDefinitionList = new ArrayList<DraftChoiceDefinition>();
        for (JSONObject switchResultObject : (Iterable<JSONObject>) switchResult)
            draftChoiceDefinitionList.add(constructDraftChoiceDefinition(switchResultObject));

        return new DraftChoiceDefinition() {
            @Override
            public Iterable<SoloDraft.DraftChoice> getDraftChoice(long seed, int stage) {
                Random rnd = new Random(seed + selectionSeed);
                return draftChoiceDefinitionList.get(rnd.nextInt(draftChoiceDefinitionList.size())).getDraftChoice(seed, stage);
            }

            @Override
            public CardCollection getCardsForChoiceId(String choiceId, long seed, int stage) {
                Random rnd = new Random(seed + selectionSeed);
                return draftChoiceDefinitionList.get(rnd.nextInt(draftChoiceDefinitionList.size())).getCardsForChoiceId(choiceId, seed, stage);
            }
        };
    }
}
