package com.gempukku.lotro.cards.build.field.effect.appender.resolver;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.appender.DelayedAppender;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.game.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.game.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.game.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.game.effects.choose.ChooseCardsFromDeckEffect;
import com.gempukku.lotro.game.effects.choose.ChooseCardsFromDiscardEffect;
import com.gempukku.lotro.game.effects.choose.ChooseCardsFromHandEffect;
import com.gempukku.lotro.game.effects.choose.ChooseStackedCardsEffect;
import com.gempukku.lotro.game.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.game.effects.AbstractEffect;
import com.gempukku.lotro.game.effects.Effect;
import com.gempukku.lotro.game.effects.UnrespondableEffect;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class CardResolver {
    public static EffectAppender resolveStackedCards(String type, ValueSource countSource, FilterableSource stackedOn,
                                                     String memory, String choicePlayer, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        return resolveStackedCards(type, null, countSource, stackedOn, memory, choicePlayer, choiceText, environment);
    }

    public static EffectAppender resolveStackedCards(String type, FilterableSource additionalFilter, ValueSource countSource, FilterableSource stackedOn,
                                                     String memory, String choicePlayer, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        return resolveStackedCards(type, additionalFilter, additionalFilter, countSource, stackedOn, memory, choicePlayer, choiceText, environment);
    }

    public static EffectAppender resolveStackedCards(String type, FilterableSource choiceFilter, FilterableSource playabilityFilter, ValueSource countSource, FilterableSource stackedOn,
                                                     String memory, String choicePlayer, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        Function<ActionContext, Iterable<? extends LotroPhysicalCard>> cardSource = actionContext -> {
            final Filterable stackedOnFilter = stackedOn.getFilterable(actionContext);
            return Filters.filter(actionContext.getGame().getGameState().getAllCards(), actionContext.getGame(), Filters.stackedOn(stackedOnFilter));
        };

        if (type.startsWith("memory(") && type.endsWith(")")) {
            return resolveMemoryCards(type, choiceFilter, playabilityFilter, countSource, memory, cardSource);
        }
        else if (type.startsWith("choose(") && type.endsWith(")")) {
            final PlayerSource playerSource = PlayerResolver.resolvePlayer(choicePlayer, environment);
            ChoiceEffectSource effectSource = (possibleCards, action, actionContext, min, max) -> {
                String choicePlayerId = playerSource.getPlayer(actionContext);
                return new ChooseStackedCardsEffect(action, choicePlayerId, min, max, stackedOn.getFilterable(actionContext), Filters.in(possibleCards)) {
                    @Override
                    protected void cardsChosen(DefaultGame game, Collection<LotroPhysicalCard> stackedCards) {
                        actionContext.setCardMemory(memory, stackedCards);
                    }

                    @Override
                    public String getText(DefaultGame game) {
                        return GameUtils.SubstituteText(choiceText, actionContext);
                    }
                };
            };

            return resolveChoiceCards(type, choiceFilter, playabilityFilter, countSource, environment, cardSource, effectSource);
        }
        throw new RuntimeException("Unable to resolve card resolver of type: " + type);
    }

    public static EffectAppender resolveCardsInHand(String type, ValueSource countSource, String memory, String choicePlayer, String handPlayer, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        return resolveCardsInHand(type, null, countSource, memory, choicePlayer, handPlayer, choiceText, false, environment);
    }

    public static EffectAppender resolveCardsInHand(String type, ValueSource countSource, String memory, String choicePlayer, String handPlayer, String choiceText, boolean showMatchingOnly, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        return resolveCardsInHand(type, null, countSource, memory, choicePlayer, handPlayer, choiceText, showMatchingOnly, environment);
    }

    public static EffectAppender resolveCardsInHand(String type, FilterableSource additionalFilter, ValueSource countSource, String memory, String choicePlayer, String handPlayer, String choiceText, boolean showMatchingOnly, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        final PlayerSource handSource = PlayerResolver.resolvePlayer(handPlayer, environment);
        Function<ActionContext, Iterable<? extends LotroPhysicalCard>> cardSource = actionContext -> {
            String handPlayer1 = handSource.getPlayer(actionContext);
            return actionContext.getGame().getGameState().getHand(handPlayer1);
        };

        if (type.startsWith("random(") && type.endsWith(")")) {
            final int count = Integer.parseInt(type.substring(type.indexOf("(") + 1, type.lastIndexOf(")")));
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(ActionContext actionContext) {
                    final String handPlayer = handSource.getPlayer(actionContext);
                    return actionContext.getGame().getGameState().getHand(handPlayer).size() >= count;
                }

                @Override
                protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                    final String handPlayer = handSource.getPlayer(actionContext);
                    return new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(DefaultGame game) {
                            List<? extends LotroPhysicalCard> hand = game.getGameState().getHand(handPlayer);
                            List<LotroPhysicalCard> randomCardsFromHand = GameUtils.getRandomCards(hand, 2);
                            actionContext.setCardMemory(memory, randomCardsFromHand);
                        }
                    };
                }
            };
        } else if (type.equals("self")) {
            return resolveSelf(additionalFilter, additionalFilter, countSource, memory, cardSource);
        } else if (type.startsWith("memory(") && type.endsWith(")")) {
            return resolveMemoryCards(type, additionalFilter, additionalFilter, countSource, memory, cardSource);
        } else if (type.startsWith("all(") && type.endsWith(")")) {
            return resolveAllCards(type, additionalFilter, memory, environment, cardSource);
        } else if (type.startsWith("choose(") && type.endsWith(")")) {
            final PlayerSource playerSource = PlayerResolver.resolvePlayer(choicePlayer, environment);
            ChoiceEffectSource effectSource = (possibleCards, action, actionContext, min, max) -> {
                String handId = handSource.getPlayer(actionContext);
                String choicePlayerId = playerSource.getPlayer(actionContext);
                if (handId.equals(choicePlayerId)) {
                    return new ChooseCardsFromHandEffect(choicePlayerId, min, max, Filters.in(possibleCards)) {
                        @Override
                        protected void cardsSelected(DefaultGame game, Collection<LotroPhysicalCard> cards) {
                            actionContext.setCardMemory(memory, cards);
                        }

                        @Override
                        public String getText(DefaultGame game) {
                            return GameUtils.SubstituteText(choiceText, actionContext);
                        }
                    };
                } else {
                    List<? extends LotroPhysicalCard> cardsInHand = actionContext.getGame().getGameState().getHand(handId);
                    return new ChooseArbitraryCardsEffect(choicePlayerId, GameUtils.SubstituteText(choiceText, actionContext), cardsInHand, Filters.in(possibleCards), min, max, showMatchingOnly) {
                        @Override
                        protected void cardsSelected(DefaultGame game, Collection<LotroPhysicalCard> selectedCards) {
                            actionContext.setCardMemory(memory, selectedCards);
                        }
                    };
                }
            };

            return resolveChoiceCards(type, additionalFilter, additionalFilter, countSource, environment, cardSource, effectSource);
        }
        throw new RuntimeException("Unable to resolve card resolver of type: " + type);
    }

    public static EffectAppender resolveCardsInDiscard(String type, ValueSource countSource, String memory, String choicePlayer, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        return resolveCardsInDiscard(type, null, countSource, memory, choicePlayer, choiceText, environment);
    }

    public static EffectAppender resolveCardsInDiscard(String type, ValueSource countSource, String memory, String choicePlayer, String targetPlayerDiscard, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        return resolveCardsInDiscard(type, null, countSource, memory, choicePlayer, targetPlayerDiscard, choiceText, environment);
    }

    public static EffectAppender resolveCardsInDiscard(String type, FilterableSource additionalFilter, ValueSource countSource, String memory, String choicePlayer, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        return resolveCardsInDiscard(type, additionalFilter, additionalFilter, countSource, memory, choicePlayer, choiceText, environment);
    }

    public static EffectAppender resolveCardsInDiscard(String type, FilterableSource additionalFilter, ValueSource countSource, String memory, String choicePlayer, String targetPlayerDiscard, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        return resolveCardsInDiscard(type, additionalFilter, additionalFilter, countSource, memory, choicePlayer,  targetPlayerDiscard, choiceText, environment);
    }


    public static EffectAppender resolveCardsInDiscard(String type, FilterableSource choiceFilter, FilterableSource playabilityFilter, ValueSource countSource, String memory, String choicePlayer, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        return resolveCardsInDiscard(type, choiceFilter, playabilityFilter, countSource, memory, choicePlayer, choicePlayer, choiceText, environment);
    }

    public static EffectAppender resolveCardsInDiscard(String type, FilterableSource choiceFilter, FilterableSource playabilityFilter, ValueSource countSource, String memory, String choicePlayer, String targetPlayerDiscard, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        final PlayerSource playerSource = PlayerResolver.resolvePlayer(choicePlayer, environment);
        final PlayerSource targetPlayerDiscardSource = PlayerResolver.resolvePlayer(targetPlayerDiscard, environment);

        Function<ActionContext, Iterable<? extends LotroPhysicalCard>> cardSource = actionContext -> {
            String targetPlayerId = targetPlayerDiscardSource.getPlayer(actionContext);
            return actionContext.getGame().getGameState().getDiscard(targetPlayerId);
        };

        if (type.equals("self")) {
            return resolveSelf(choiceFilter, playabilityFilter, countSource, memory, cardSource);
        } else if (type.startsWith("memory(") && type.endsWith(")")) {
            return resolveMemoryCards(type, choiceFilter, playabilityFilter, countSource, memory, cardSource);
        } else if (type.startsWith("all(") && type.endsWith(")")) {
            return resolveAllCards(type, choiceFilter, memory, environment, cardSource);
        } else if (type.startsWith("choose(") && type.endsWith(")")) {
            ChoiceEffectSource effectSource = (possibleCards, action, actionContext, min, max) -> {
                String choicePlayerId = playerSource.getPlayer(actionContext);
                String targetPlayerDiscardId = targetPlayerDiscardSource.getPlayer(actionContext);
                return new ChooseCardsFromDiscardEffect(choicePlayerId, targetPlayerDiscardId, min, max, Filters.in(possibleCards)) {
                    @Override
                    protected void cardsSelected(DefaultGame game, Collection<LotroPhysicalCard> cards) {
                        actionContext.setCardMemory(memory, cards);
                    }

                    @Override
                    public String getText(DefaultGame game) {
                        return GameUtils.SubstituteText(choiceText, actionContext);
                    }
                };
            };

            return resolveChoiceCards(type, choiceFilter, playabilityFilter, countSource, environment, cardSource, effectSource);
        }
        throw new RuntimeException("Unable to resolve card resolver of type: " + type);
    }

    public static EffectAppender resolveCardsInDeck(String type, ValueSource countSource, String memory, String choicePlayer, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        return resolveCardsInDeck(type, null, countSource, memory, choicePlayer, choicePlayer, choiceText, environment);
    }

    public static EffectAppender resolveCardsInDeck(String type, FilterableSource choiceFilter, ValueSource countSource, String memory, String choicePlayer, String targetDeck, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        final PlayerSource playerSource = PlayerResolver.resolvePlayer(choicePlayer, environment);
        final PlayerSource deckSource = PlayerResolver.resolvePlayer(targetDeck, environment);

        Function<ActionContext, Iterable<? extends LotroPhysicalCard>> cardSource = actionContext -> {
            String deckId = deckSource.getPlayer(actionContext);
            return actionContext.getGame().getGameState().getDeck(deckId);
        };

        if (type.startsWith("memory(") && type.endsWith(")")) {
            return resolveMemoryCards(type, choiceFilter, choiceFilter, countSource, memory, cardSource);
        } else if (type.startsWith("all(") && type.endsWith(")")) {
            return resolveAllCards(type, choiceFilter, memory, environment, cardSource);
        } else if (type.startsWith("choose(") && type.endsWith(")")) {
            ChoiceEffectSource effectSource = (possibleCards, action, actionContext, min, max) -> {
                String choicePlayerId = playerSource.getPlayer(actionContext);
                String targetDeckId = deckSource.getPlayer(actionContext);
                return new ChooseCardsFromDeckEffect(choicePlayerId, targetDeckId, min, max, Filters.in(possibleCards)) {
                    @Override
                    protected void cardsSelected(DefaultGame game, Collection<LotroPhysicalCard> cards) {
                        actionContext.setCardMemory(memory, cards);
                    }

                    @Override
                    public String getText(DefaultGame game) {
                        return GameUtils.SubstituteText(choiceText, actionContext);
                    }
                };
            };

            return resolveChoiceCards(type, choiceFilter, choiceFilter, countSource, environment, cardSource, effectSource);
        }
        throw new RuntimeException("Unable to resolve card resolver of type: " + type);
    }

    public static EffectAppender resolveCard(String type, FilterableSource additionalFilter, String memory, String choicePlayer, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        return resolveCards(type, additionalFilter, new ConstantEvaluator(1), memory, choicePlayer, choiceText, environment);
    }

    public static EffectAppender resolveCard(String type, String memory, String choicePlayer, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        return resolveCard(type, null, memory, choicePlayer, choiceText, environment);
    }

    public static EffectAppender resolveCards(String type, ValueSource countSource, String memory, String choicePlayer, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        return resolveCards(type, null, countSource, memory, choicePlayer, choiceText, environment);
    }

    public static EffectAppender resolveCards(String type, FilterableSource additionalFilter, ValueSource countSource, String memory, String choicePlayer, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        return resolveCards(type, additionalFilter, additionalFilter, countSource, memory, choicePlayer, choiceText, environment);
    }

    public static EffectAppender resolveCards(String type, FilterableSource additionalFilter, FilterableSource playabilityFilter, ValueSource countSource, String memory, String choicePlayer, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        Function<ActionContext, Iterable<? extends LotroPhysicalCard>> cardSource = actionContext ->
                Filters.filterActive(actionContext.getGame(), Filters.any);

        if (type.equals("self")) {
            return resolveSelf(additionalFilter, playabilityFilter, countSource, memory, cardSource);
        } else if (type.equals("bearer")) {
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(ActionContext actionContext) {
                    int min = countSource.getMinimum(actionContext);
                    return filterCards(actionContext, playabilityFilter).size() >= min;
                }

                @Override
                protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                    Collection<LotroPhysicalCard> result = filterCards(actionContext, additionalFilter);
                    return new AbstractEffect() {
                        @Override
                        public boolean isPlayableInFull(DefaultGame game) {
                            int min = countSource.getMinimum(actionContext);
                            return result.size() >= min;
                        }

                        @Override
                        protected FullEffectResult playEffectReturningResult(DefaultGame game) {
                            actionContext.setCardMemory(memory, result);
                            int min = countSource.getMinimum(actionContext);
                            if (result.size() >= min) {
                                return new FullEffectResult(true);
                            } else {
                                return new FullEffectResult(false);
                            }
                        }
                    };
                }

                private Collection<LotroPhysicalCard> filterCards(ActionContext actionContext, FilterableSource filter) {
                    LotroPhysicalCard source = actionContext.getSource();
                    LotroPhysicalCard attachedTo = source.getAttachedTo();
                    if (attachedTo == null)
                        return Collections.emptySet();
                    
                    Filterable additionalFilterable = Filters.any;
                    if (filter != null)
                        additionalFilterable = filter.getFilterable(actionContext);
                    return Filters.filter(cardSource.apply(actionContext), actionContext.getGame(), attachedTo, additionalFilterable);
                }
            };
        } else if (type.startsWith("memory(") && type.endsWith(")")) {
            return resolveMemoryCards(type, additionalFilter, playabilityFilter, countSource, memory, cardSource);
        } else if (type.startsWith("all(") && type.endsWith(")")) {
            return resolveAllCards(type, additionalFilter, memory, environment, cardSource);
        } else if (type.startsWith("choose(") && type.endsWith(")")) {
            final PlayerSource playerSource = PlayerResolver.resolvePlayer(choicePlayer, environment);
            ChoiceEffectSource effectSource = (possibleCards, action, actionContext, min, max) -> {
                String choicePlayerId = playerSource.getPlayer(actionContext);
                return new ChooseActiveCardsEffect(actionContext.getSource(), choicePlayerId, GameUtils.SubstituteText(choiceText, actionContext), min, max, Filters.in(possibleCards)) {
                    @Override
                    protected void cardsSelected(DefaultGame game, Collection<LotroPhysicalCard> cards) {
                        actionContext.setCardMemory(memory, cards);
                    }
                };
            };

            return resolveChoiceCards(type, additionalFilter, playabilityFilter, countSource, environment, cardSource, effectSource);
        }
        throw new InvalidCardDefinitionException("Unable to resolve card resolver of type: " + type);
    }

    private static DelayedAppender resolveSelf(FilterableSource choiceFilter, FilterableSource playabilityFilter,
                                               ValueSource countSource, String memory,
                                               Function<ActionContext, Iterable<? extends LotroPhysicalCard>> cardSource) {
        return new DelayedAppender() {
            @Override
            public boolean isPlayableInFull(ActionContext actionContext) {
                int min = countSource.getMinimum(actionContext);
                return filterCards(actionContext, playabilityFilter).size() >= min;
            }

            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                Collection<LotroPhysicalCard> result = filterCards(actionContext, choiceFilter);
                return new AbstractEffect() {
                    @Override
                    public boolean isPlayableInFull(DefaultGame game) {
                        int min = countSource.getMinimum(actionContext);
                        return result.size() >= min;
                    }

                    @Override
                    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
                        actionContext.setCardMemory(memory, result);
                        int min = countSource.getMinimum(actionContext);
                        if (result.size() >= min) {
                            return new FullEffectResult(true);
                        } else {
                            return new FullEffectResult(false);
                        }
                    }
                };
            }

            private Collection<LotroPhysicalCard> filterCards(ActionContext actionContext, FilterableSource filter) {
                LotroPhysicalCard source = actionContext.getSource();
                Filterable additionalFilterable = Filters.any;
                if (filter != null)
                    additionalFilterable = filter.getFilterable(actionContext);
                return Filters.filter(cardSource.apply(actionContext), actionContext.getGame(), source, additionalFilterable);
            }
        };
    }

    private static DelayedAppender resolveMemoryCards(String type, FilterableSource choiceFilter, FilterableSource playabilityFilter,
                                                      ValueSource countSource, String memory,
                                                      Function<ActionContext, Iterable<? extends LotroPhysicalCard>> cardSource) throws InvalidCardDefinitionException {
        String sourceMemory = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
        if (sourceMemory.contains("(") || sourceMemory.contains(")"))
            throw new InvalidCardDefinitionException("Memory name cannot contain parenthesis");

        return new DelayedAppender() {
            @Override
            public boolean isPlayableInFull(ActionContext actionContext) {
                if (playabilityFilter != null) {
                    int min = countSource.getMinimum(actionContext);
                    return filterCards(actionContext, playabilityFilter).size() >= min;
                } else {
                    return true;
                }
            }

            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                Collection<LotroPhysicalCard> result = filterCards(actionContext, choiceFilter);
                return new AbstractEffect() {
                    @Override
                    public boolean isPlayableInFull(DefaultGame game) {
                        int min = countSource.getMinimum(actionContext);
                        return result.size() >= min;
                    }

                    @Override
                    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
                        actionContext.setCardMemory(memory, result);
                        int min = countSource.getMinimum(actionContext);
                        if (result.size() >= min) {
                            return new FullEffectResult(true);
                        } else {
                            return new FullEffectResult(false);
                        }
                    }
                };
            }

            private Collection<LotroPhysicalCard> filterCards(ActionContext actionContext, FilterableSource filter) {
                Collection<? extends LotroPhysicalCard> cardsFromMemory = actionContext.getCardsFromMemory(sourceMemory);
                Filterable additionalFilterable = Filters.any;
                if (filter != null)
                    additionalFilterable = filter.getFilterable(actionContext);
                return Filters.filter(cardSource.apply(actionContext), actionContext.getGame(), Filters.in(cardsFromMemory), additionalFilterable);
            }
        };
    }

    private static DelayedAppender resolveChoiceCards(String type, FilterableSource choiceFilter, FilterableSource playabilityFilter,
                                                      ValueSource countSource, CardGenerationEnvironment environment, Function<ActionContext, Iterable<? extends LotroPhysicalCard>> cardSource, ChoiceEffectSource effectSource) throws InvalidCardDefinitionException {
        final String filter = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);

        return new DelayedAppender() {
            @Override
            public boolean isPlayableInFull(ActionContext actionContext) {
                int min = countSource.getMinimum(actionContext);
                return filterCards(actionContext, playabilityFilter).size() >= min;
            }

            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                int min = countSource.getMinimum(actionContext);
                int max = countSource.getMaximum(actionContext);
                return effectSource.createEffect(filterCards(actionContext, choiceFilter), action, actionContext, min, max);
            }

            private Collection<LotroPhysicalCard> filterCards(ActionContext actionContext, FilterableSource filter) {
                Filterable filterable = filterableSource.getFilterable(actionContext);
                Filterable additionalFilterable = Filters.any;
                if (filter != null)
                    additionalFilterable = filter.getFilterable(actionContext);
                return Filters.filter(cardSource.apply(actionContext), actionContext.getGame(), filterable, additionalFilterable);
            }
        };
    }

    private static DelayedAppender resolveAllCards(String type, FilterableSource additionalFilter, String memory, CardGenerationEnvironment environment, Function<ActionContext, Iterable<? extends LotroPhysicalCard>> cardSource) throws InvalidCardDefinitionException {
        final String filter = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);
        return new DelayedAppender() {
            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                return new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(DefaultGame game) {
                        actionContext.setCardMemory(memory, filterCards(actionContext, additionalFilter));
                    }

                    private Collection<LotroPhysicalCard> filterCards(ActionContext actionContext, FilterableSource filter) {
                        final Filterable filterable = filterableSource.getFilterable(actionContext);
                        Filterable additionalFilterable = Filters.any;
                        if (filter != null)
                            additionalFilterable = filter.getFilterable(actionContext);
                        return Filters.filter(cardSource.apply(actionContext), actionContext.getGame(), filterable, additionalFilterable);
                    }
                };
            }
        };
    }

    private interface ChoiceEffectSource {
        Effect createEffect(Collection<? extends LotroPhysicalCard> possibleCards, CostToEffectAction action, ActionContext actionContext,
                            int min, int max);
    }
}
