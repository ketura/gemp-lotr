package com.gempukku.lotro.cards.build.field.effect.appender.resolver;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.FilterableSource;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.appender.AbstractEffectAppender;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collection;

public class CardResolver {
    public static EffectAppender resolveCard(String type, FilterableSource additionalFilter, String memory, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        if (type.equals("self")) {
            return new AbstractEffectAppender() {
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
            return new AbstractEffectAppender() {
                @Override
                protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    return new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            action.setCardMemory(memory, action.getCardFromMemory(sourceMemory));
                        }
                    };
                }
            };
        } else if (type.startsWith("choose(") && type.endsWith(")")) {
            final String filter = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);
            return new AbstractEffectAppender() {
                @Override
                protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    final Filterable filterable = filterableSource.getFilterable(playerId, game, self, effectResult, effect);
                    Filterable additionalFilterable = Filters.any;
                    if (additionalFilter != null)
                        additionalFilterable = additionalFilter.getFilterable(playerId, game, self, effectResult, effect);
                    return new ChooseActiveCardEffect(self, playerId, choiceText, filterable, additionalFilterable) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.setCardMemory(memory, card);
                        }
                    };
                }
            };
        }
        throw new RuntimeException("Unable to resolve card resolver of type: " + type);
    }

    public static EffectAppender resolveCard(String type, String memory, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        return resolveCard(type, null, memory, choiceText, environment);
    }

    public static EffectAppender resolveCards(String type, FilterableSource additionalFilter, int min, int max, String memory, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        if (type.equals("self")) {
            return new AbstractEffectAppender() {
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
            return new AbstractEffectAppender() {
                @Override
                protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    return new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            action.setCardMemory(memory, action.getCardFromMemory(sourceMemory));
                        }
                    };
                }
            };
        } else if (type.startsWith("choose(") && type.endsWith(")")) {
            final String filter = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);
            return new AbstractEffectAppender() {
                @Override
                protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                    final Filterable filterable = filterableSource.getFilterable(playerId, game, self, effectResult, effect);
                    Filterable additionalFilterable = Filters.any;
                    if (additionalFilter != null)
                        additionalFilterable = additionalFilter.getFilterable(playerId, game, self, effectResult, effect);
                    return new ChooseActiveCardsEffect(self, playerId, choiceText, min, max, filterable, additionalFilterable) {
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

    public static EffectAppender resolveCards(String type, int min, int max, String memory, String choiceText, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        return resolveCards(type, null, min, max, memory, choiceText, environment);
    }
}
