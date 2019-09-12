package com.gempukku.lotro.cards.build.field.effect.appender.resolver;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.appender.DelayedAppender;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseCardsFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseCardsFromDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseStackedCardsEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
        if (type.startsWith("memory(") && type.endsWith(")")) {
            String sourceMemory = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(ActionContext actionContext) {
                    return true;
                }

                @Override
                protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                    return new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            actionContext.setCardMemory(memory, actionContext.getCardsFromMemory(sourceMemory));
                        }
                    };
                }
            };
        } else if (type.startsWith("choose(") && type.endsWith(")")) {
            final String filter = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);
            final PlayerSource playerSource = PlayerResolver.resolvePlayer(choicePlayer, environment);
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(ActionContext actionContext) {
                    final int min = countSource.getMinimum(actionContext);
                    final Filterable filterable = filterableSource.getFilterable(actionContext);
                    final Filterable stackedOnFilter = stackedOn.getFilterable(actionContext);

                    Filterable additionalFilterable = Filters.any;
                    if (playabilityFilter != null)
                        additionalFilterable = playabilityFilter.getFilterable(actionContext);

                    List<PhysicalCard> choice = new LinkedList<>();

                    final LotroGame game = actionContext.getGame();
                    for (PhysicalCard stackedOn : Filters.filterActive(game, stackedOnFilter)) {
                        final List<PhysicalCard> stackedCards = game.getGameState().getStackedCards(stackedOn);
                        choice.addAll(Filters.filter(stackedCards, game, filterable, additionalFilterable));
                    }

                    return choice.size() >= min;
                }

                @Override
                protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                    final Filterable filterable = filterableSource.getFilterable(actionContext);
                    final Filterable stackedOnFilter = stackedOn.getFilterable(actionContext);
                    String choicePlayerId = playerSource.getPlayer(actionContext);
                    int min = countSource.getMinimum(actionContext);
                    int max = countSource.getMaximum(actionContext);

                    Filterable additionalFilterable = Filters.any;
                    if (choiceFilter != null)
                        additionalFilterable = choiceFilter.getFilterable(actionContext);

                    return new ChooseStackedCardsEffect(action, choicePlayerId, min, max, stackedOnFilter, Filters.and(filterable, additionalFilterable)) {
                        @Override
                        protected void cardsChosen(LotroGame game, Collection<PhysicalCard> stackedCards) {
                            actionContext.setCardMemory(memory, stackedCards);
                        }
                    };
                }
            };
        }
        throw new RuntimeException("Unable to resolve card resolver of type: " + type);
    }

    public static EffectAppender resolveCardsInHand(String type, ValueSource countSource, String memory, String choicePlayer, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        return resolveCardsInHand(type, null, countSource, memory, choicePlayer, choiceText, environment);
    }

    public static EffectAppender resolveCardsInHand(String type, FilterableSource additionalFilter, ValueSource countSource, String memory, String choicePlayer, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        if (type.equals("self")) {
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(ActionContext actionContext) {
                    return actionContext.getSource().getZone() == Zone.HAND;
                }

                @Override
                protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                    return new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            actionContext.setCardMemory(memory, actionContext.getSource());
                        }
                    };
                }
            };
        } else if (type.startsWith("memory(") && type.endsWith(")")) {
            final PlayerSource playerSource = PlayerResolver.resolvePlayer(choicePlayer, environment);

            String sourceMemory = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(ActionContext actionContext) {
                    if (additionalFilter != null) {
                        int min = countSource.getMinimum(null);
                        String choicePlayerId = playerSource.getPlayer(actionContext);
                        final Collection<? extends PhysicalCard> cardsFromMemory = actionContext.getCardsFromMemory(sourceMemory);
                        Filterable filter = additionalFilter.getFilterable(actionContext);
                        final LotroGame game = actionContext.getGame();
                        return Filters.filter(game.getGameState().getHand(choicePlayerId), game, filter, Filters.in(cardsFromMemory)).size() >= min;
                    }
                    return true;
                }

                @Override
                protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                    return new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            actionContext.setCardMemory(memory, actionContext.getCardsFromMemory(sourceMemory));
                        }
                    };
                }
            };
        } else if (type.startsWith("choose(") && type.endsWith(")")) {
            final String filter = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);
            final PlayerSource playerSource = PlayerResolver.resolvePlayer(choicePlayer, environment);
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(ActionContext actionContext) {
                    int min = countSource.getMinimum(actionContext);
                    final Filterable filterable = filterableSource.getFilterable(actionContext);
                    String choicePlayerId = playerSource.getPlayer(actionContext);
                    Filterable additionalFilterable = Filters.any;
                    if (additionalFilter != null)
                        additionalFilterable = additionalFilter.getFilterable(actionContext);
                    final LotroGame game = actionContext.getGame();
                    return Filters.filter(game.getGameState().getHand(choicePlayerId), game, filterable, additionalFilterable).size() >= min;
                }

                @Override
                protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                    int min = countSource.getMinimum(actionContext);
                    int max = countSource.getMaximum(actionContext);
                    final Filterable filterable = filterableSource.getFilterable(actionContext);
                    String choicePlayerId = playerSource.getPlayer(actionContext);
                    Filterable additionalFilterable = Filters.any;
                    if (additionalFilter != null)
                        additionalFilterable = additionalFilter.getFilterable(actionContext);
                    return new ChooseCardsFromHandEffect(choicePlayerId, min, max, filterable, additionalFilterable) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                            actionContext.setCardMemory(memory, cards);
                        }
                    };
                }
            };
        }
        throw new RuntimeException("Unable to resolve card resolver of type: " + type);
    }

    public static EffectAppender resolveCardsInDiscard(String type, ValueSource countSource, String memory, String choicePlayer, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        return resolveCardsInDiscard(type, null, countSource, memory, choicePlayer, choiceText, environment);
    }

    public static EffectAppender resolveCardsInDiscard(String type, FilterableSource additionalFilter, ValueSource countSource, String memory, String choicePlayer, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        return resolveCardsInDiscard(type, additionalFilter, additionalFilter, countSource, memory, choicePlayer, choiceText, environment);
    }

    public static EffectAppender resolveCardsInDiscard(String type, FilterableSource choiceFilter, FilterableSource playabilityFilter, ValueSource countSource, String memory, String choicePlayer, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        if (type.startsWith("memory(") && type.endsWith(")")) {
            final PlayerSource playerSource = PlayerResolver.resolvePlayer(choicePlayer, environment);

            String sourceMemory = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(ActionContext actionContext) {
                    if (playabilityFilter != null) {
                        int min = countSource.getMinimum(null);
                        String choicePlayerId = playerSource.getPlayer(actionContext);
                        final Collection<? extends PhysicalCard> cardsFromMemory = actionContext.getCardsFromMemory(sourceMemory);
                        Filterable filter = playabilityFilter.getFilterable(actionContext);
                        final LotroGame game = actionContext.getGame();
                        return Filters.filter(game.getGameState().getDiscard(choicePlayerId), game, filter, Filters.in(cardsFromMemory)).size() >= min;
                    }
                    return true;
                }

                @Override
                protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                    return new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            actionContext.setCardMemory(memory, actionContext.getCardsFromMemory(sourceMemory));
                        }
                    };
                }
            };
        } else if (type.startsWith("choose(") && type.endsWith(")")) {
            final String filter = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);
            final PlayerSource playerSource = PlayerResolver.resolvePlayer(choicePlayer, environment);
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(ActionContext actionContext) {
                    int min = countSource.getMinimum(actionContext);
                    final Filterable filterable = filterableSource.getFilterable(actionContext);
                    String choicePlayerId = playerSource.getPlayer(actionContext);
                    Filterable additionalFilterable = Filters.any;
                    if (playabilityFilter != null)
                        additionalFilterable = playabilityFilter.getFilterable(actionContext);
                    final LotroGame game = actionContext.getGame();
                    return Filters.filter(game.getGameState().getDiscard(choicePlayerId), game, filterable, additionalFilterable).size() >= min;
                }

                @Override
                protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                    int min = countSource.getMinimum(actionContext);
                    int max = countSource.getMaximum(actionContext);
                    final Filterable filterable = filterableSource.getFilterable(actionContext);
                    String choicePlayerId = playerSource.getPlayer(actionContext);
                    Filterable additionalFilterable = Filters.any;
                    if (choiceFilter != null)
                        additionalFilterable = choiceFilter.getFilterable(actionContext);
                    return new ChooseCardsFromDiscardEffect(choicePlayerId, min, max, filterable, additionalFilterable) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                            actionContext.setCardMemory(memory, cards);
                        }
                    };
                }
            };
        }
        throw new RuntimeException("Unable to resolve card resolver of type: " + type);
    }

    public static EffectAppender resolveCardsInDeck(String type, FilterableSource choiceFilter, ValueSource countSource, String memory, String choicePlayer, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        if (type.startsWith("memory(") && type.endsWith(")")) {
            String sourceMemory = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(ActionContext actionContext) {
                    return true;
                }

                @Override
                protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                    return new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            actionContext.setCardMemory(memory, actionContext.getCardsFromMemory(sourceMemory));
                        }
                    };
                }
            };
        } else if (type.startsWith("choose(") && type.endsWith(")")) {
            final String filter = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);
            final PlayerSource playerSource = PlayerResolver.resolvePlayer(choicePlayer, environment);
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(ActionContext actionContext) {
                    return true;
                }

                @Override
                protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                    int min = countSource.getMinimum(actionContext);
                    int max = countSource.getMaximum(actionContext);
                    final Filterable filterable = filterableSource.getFilterable(actionContext);
                    String choicePlayerId = playerSource.getPlayer(actionContext);
                    Filterable additionalFilterable = Filters.any;
                    if (choiceFilter != null)
                        additionalFilterable = choiceFilter.getFilterable(actionContext);
                    return new ChooseCardsFromDeckEffect(choicePlayerId, min, max, filterable, additionalFilterable) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                            actionContext.setCardMemory(memory, cards);
                        }
                    };
                }
            };
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
        if (type.equals("self")) {
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(ActionContext actionContext) {
                    if (playabilityFilter != null)
                        return PlayConditions.isActive(actionContext.getGame(), actionContext.getSource(), playabilityFilter.getFilterable(actionContext));
                    return true;
                }

                @Override
                protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                    return new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            actionContext.setCardMemory(memory, actionContext.getSource());
                        }
                    };
                }
            };
        } else if (type.startsWith("memory(") && type.endsWith(")")) {
            String sourceMemory = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(ActionContext actionContext) {
                    if (playabilityFilter != null) {
                        final Collection<? extends PhysicalCard> cardsFromMemory = actionContext.getCardsFromMemory(sourceMemory);
                        for (PhysicalCard physicalCard : cardsFromMemory) {
                            if (!PlayConditions.isActive(actionContext.getGame(), physicalCard, playabilityFilter.getFilterable(actionContext)))
                                return false;
                        }
                    }

                    return true;
                }

                @Override
                protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                    return new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            actionContext.setCardMemory(memory, actionContext.getCardsFromMemory(sourceMemory));
                        }
                    };
                }
            };
        } else if (type.startsWith("all(") && type.endsWith(")")) {
            final String filter = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);
            return new DelayedAppender() {
                @Override
                protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                    return new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            final Filterable filterable = filterableSource.getFilterable(actionContext);
                            actionContext.setCardMemory(memory, Filters.filterActive(game, filterable));
                        }
                    };
                }

                @Override
                public boolean isPlayableInFull(ActionContext actionContext) {
                    return true;
                }
            };
        } else if (type.startsWith("choose(") && type.endsWith(")")) {
            final String filter = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);
            final PlayerSource playerSource = PlayerResolver.resolvePlayer(choicePlayer, environment);
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(ActionContext actionContext) {
                    int min = countSource.getMinimum(actionContext);
                    final Filterable filterable = filterableSource.getFilterable(actionContext);
                    Filterable additionalFilterable = Filters.any;
                    if (playabilityFilter != null)
                        additionalFilterable = playabilityFilter.getFilterable(actionContext);
                    return PlayConditions.isActive(actionContext.getGame(), min, filterable, additionalFilterable);
                }

                @Override
                protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                    int min = countSource.getMinimum(actionContext);
                    int max = countSource.getMaximum(actionContext);
                    final Filterable filterable = filterableSource.getFilterable(actionContext);
                    Filterable additionalFilterable = Filters.any;
                    if (additionalFilter != null)
                        additionalFilterable = additionalFilter.getFilterable(actionContext);
                    String choicePlayerId = playerSource.getPlayer(actionContext);
                    return new ChooseActiveCardsEffect(actionContext.getSource(), choicePlayerId, choiceText, min, max, filterable, additionalFilterable) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                            actionContext.setCardMemory(memory, cards);
                        }
                    };
                }
            };
        }
        throw new InvalidCardDefinitionException("Unable to resolve card resolver of type: " + type);
    }
}
