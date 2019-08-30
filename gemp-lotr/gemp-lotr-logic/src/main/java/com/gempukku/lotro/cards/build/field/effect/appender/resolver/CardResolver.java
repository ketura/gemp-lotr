package com.gempukku.lotro.cards.build.field.effect.appender.resolver;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.appender.DelayedAppender;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseCardsFromDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseStackedCardsEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CardResolver {
    public static EffectAppender resolveStackedCards(String type, ValueSource countSource, FilterableSource stackedOn,
                                                     String memory, String choicePlayer, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        if (type.startsWith("memory(") && type.endsWith(")")) {
            String sourceMemory = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    return true;
                }

                @Override
                protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    return new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            action.setCardMemory(memory, action.getCardsFromMemory(sourceMemory));
                        }
                    };
                }
            };
        } else if (type.startsWith("choose(") && type.endsWith(")")) {
            final String filter = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);
            final PlayerSource playerSource = PlayerResolver.resolvePlayer(choicePlayer, environment);
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    final int min = countSource.getMinimum(null, playerId, game, self, effectResult, effect);
                    final Filterable filterable = filterableSource.getFilterable(playerId, game, self, effectResult, effect);
                    final Filterable stackedOnFilter = stackedOn.getFilterable(playerId, game, self, effectResult, effect);

                    List<PhysicalCard> choice = new LinkedList<>();

                    for (PhysicalCard stackedOn : Filters.filterActive(game, stackedOnFilter)) {
                        final List<PhysicalCard> stackedCards = game.getGameState().getStackedCards(stackedOn);
                        choice.addAll(Filters.filter(stackedCards, game, filterable));
                    }

                    return choice.size() >= min;
                }

                @Override
                protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    final Filterable filterable = filterableSource.getFilterable(playerId, game, self, effectResult, effect);
                    final Filterable stackedOnFilter = stackedOn.getFilterable(playerId, game, self, effectResult, effect);
                    String choicePlayerId = playerSource.getPlayer(playerId, game, self, effectResult, effect);
                    int min = countSource.getMinimum(action, playerId, game, self, effectResult, effect);
                    int max = countSource.getMaximum(action, playerId, game, self, effectResult, effect);

                    return new ChooseStackedCardsEffect(action, choicePlayerId, min, max, stackedOnFilter, filterable) {
                        @Override
                        protected void cardsChosen(LotroGame game, Collection<PhysicalCard> stackedCards) {
                            action.setCardMemory(memory, stackedCards);
                        }
                    };
                }
            };
        }
        throw new RuntimeException("Unable to resolve card resolver of type: " + type);
    }

    public static EffectAppender resolveCardsInHand(String type, ValueSource countSource, String memory, String choicePlayer, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        if (type.startsWith("memory(") && type.endsWith(")")) {
            String sourceMemory = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    return true;
                }

                @Override
                protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    return new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            action.setCardMemory(memory, action.getCardsFromMemory(sourceMemory));
                        }
                    };
                }
            };
        } else if (type.startsWith("choose(") && type.endsWith(")")) {
            final String filter = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);
            final PlayerSource playerSource = PlayerResolver.resolvePlayer(choicePlayer, environment);
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    int min = countSource.getMinimum(null, playerId, game, self, effectResult, effect);
                    final Filterable filterable = filterableSource.getFilterable(playerId, game, self, effectResult, effect);
                    String choicePlayerId = playerSource.getPlayer(playerId, game, self, effectResult, effect);
                    return Filters.filter(game.getGameState().getHand(choicePlayerId), game, filterable).size() >= min;
                }

                @Override
                protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    int min = countSource.getMinimum(action, playerId, game, self, effectResult, effect);
                    int max = countSource.getMaximum(action, playerId, game, self, effectResult, effect);
                    final Filterable filterable = filterableSource.getFilterable(playerId, game, self, effectResult, effect);
                    String choicePlayerId = playerSource.getPlayer(playerId, game, self, effectResult, effect);
                    return new ChooseCardsFromHandEffect(choicePlayerId, min, max, filterable) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                            action.setCardMemory(memory, cards);
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
        if (type.startsWith("memory(") && type.endsWith(")")) {
            final PlayerSource playerSource = PlayerResolver.resolvePlayer(choicePlayer, environment);

            String sourceMemory = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    if (additionalFilter != null) {
                        int min = countSource.getMinimum(null, playerId, game, self, effectResult, effect);
                        String choicePlayerId = playerSource.getPlayer(playerId, game, self, effectResult, effect);
                        final Collection<? extends PhysicalCard> cardsFromMemory = action.getCardsFromMemory(sourceMemory);
                        Filterable filter = additionalFilter.getFilterable(playerId, game, self, effectResult, effect);
                        return Filters.filter(game.getGameState().getDiscard(choicePlayerId), game, filter, Filters.in(cardsFromMemory)).size() >= min;
                    }
                    return true;
                }

                @Override
                protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    return new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            action.setCardMemory(memory, action.getCardsFromMemory(sourceMemory));
                        }
                    };
                }
            };
        } else if (type.startsWith("choose(") && type.endsWith(")")) {
            final String filter = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);
            final PlayerSource playerSource = PlayerResolver.resolvePlayer(choicePlayer, environment);
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    int min = countSource.getMinimum(null, playerId, game, self, effectResult, effect);
                    final Filterable filterable = filterableSource.getFilterable(playerId, game, self, effectResult, effect);
                    String choicePlayerId = playerSource.getPlayer(playerId, game, self, effectResult, effect);
                    Filterable additionalFilterable = Filters.any;
                    if (additionalFilter != null)
                        additionalFilterable = additionalFilter.getFilterable(playerId, game, self, effectResult, effect);
                    return Filters.filter(game.getGameState().getDiscard(choicePlayerId), game, filterable, additionalFilterable).size() >= min;
                }

                @Override
                protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    int min = countSource.getMinimum(action, playerId, game, self, effectResult, effect);
                    int max = countSource.getMaximum(action, playerId, game, self, effectResult, effect);
                    final Filterable filterable = filterableSource.getFilterable(playerId, game, self, effectResult, effect);
                    String choicePlayerId = playerSource.getPlayer(playerId, game, self, effectResult, effect);
                    Filterable additionalFilterable = Filters.any;
                    if (additionalFilter != null)
                        additionalFilterable = additionalFilter.getFilterable(playerId, game, self, effectResult, effect);
                    return new ChooseCardsFromDiscardEffect(choicePlayerId, min, max, filterable, additionalFilterable) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                            action.setCardMemory(memory, cards);
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
        if (type.equals("self")) {
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    if (additionalFilter != null)
                        return PlayConditions.isActive(game, self, additionalFilter.getFilterable(playerId, game, self, effectResult, effect));
                    return true;
                }

                @Override
                protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    return new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            action.setCardMemory(memory, self);
                        }
                    };
                }
            };
        } else if (type.startsWith("memory(") && type.endsWith(")")) {
            String sourceMemory = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    if (additionalFilter != null) {
                        final Collection<? extends PhysicalCard> cardsFromMemory = action.getCardsFromMemory(sourceMemory);
                        for (PhysicalCard physicalCard : cardsFromMemory) {
                            if (!PlayConditions.isActive(game, physicalCard, additionalFilter.getFilterable(playerId, game, self, effectResult, effect)))
                                return false;
                        }
                    }

                    return true;
                }

                @Override
                protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    return new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            action.setCardMemory(memory, action.getCardsFromMemory(sourceMemory));
                        }
                    };
                }
            };
        } else if (type.startsWith("all(") && type.endsWith(")")) {
            final String filter = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);
            return new DelayedAppender() {
                @Override
                protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    return new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            final Filterable filterable = filterableSource.getFilterable(playerId, game, self, effectResult, effect);
                            action.setCardMemory(memory, Filters.filterActive(game, filterable));
                        }
                    };
                }

                @Override
                public boolean isPlayableInFull(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    return true;
                }
            };
        } else if (type.startsWith("choose(") && type.endsWith(")")) {
            final String filter = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);
            final PlayerSource playerSource = PlayerResolver.resolvePlayer(choicePlayer, environment);
            return new DelayedAppender() {
                @Override
                public boolean isPlayableInFull(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    int min = countSource.getMinimum(null, playerId, game, self, effectResult, effect);
                    final Filterable filterable = filterableSource.getFilterable(playerId, game, self, effectResult, effect);
                    Filterable additionalFilterable = Filters.any;
                    if (additionalFilter != null)
                        additionalFilterable = additionalFilter.getFilterable(playerId, game, self, effectResult, effect);
                    return PlayConditions.isActive(game, min, filterable, additionalFilterable);
                }

                @Override
                protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    int min = countSource.getMinimum(action, playerId, game, self, effectResult, effect);
                    int max = countSource.getMaximum(action, playerId, game, self, effectResult, effect);
                    final Filterable filterable = filterableSource.getFilterable(playerId, game, self, effectResult, effect);
                    Filterable additionalFilterable = Filters.any;
                    if (additionalFilter != null)
                        additionalFilterable = additionalFilter.getFilterable(playerId, game, self, effectResult, effect);
                    String choicePlayerId = playerSource.getPlayer(playerId, game, self, effectResult, effect);
                    return new ChooseActiveCardsEffect(self, choicePlayerId, choiceText, min, max, filterable, additionalFilterable) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                            action.setCardMemory(memory, cards);
                        }
                    };
                }
            };
        }
        throw new InvalidCardDefinitionException("Unable to resolve card resolver of type: " + type);
    }
}
