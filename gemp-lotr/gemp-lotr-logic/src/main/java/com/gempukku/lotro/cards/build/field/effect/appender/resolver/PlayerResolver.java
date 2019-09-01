package com.gempukku.lotro.cards.build.field.effect.appender.resolver;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.appender.AbstractEffectAppender;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collection;

public class PlayerResolver {
    public static PlayerSource resolvePlayer(String type, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        if (type.equals("owner"))
            return (actionContext) -> actionContext.getSource().getOwner();
        else if (type.equals("shadowPlayer"))
            return (actionContext) -> GameUtils.getFirstShadowPlayer(actionContext.getGame());
        else if (type.equals("fp"))
            return ((actionContext) -> actionContext.getGame().getGameState().getCurrentPlayerId());
        else if (type.startsWith("ownerFromMemory(") && type.endsWith(")")) {
            String memory = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            return (actionContext) -> actionContext.getCardFromMemory(memory).getOwner();
        }
        throw new InvalidCardDefinitionException("Unable to resolve player resolver of type: " + type);
    }

    public static EffectAppender resolvePlayer(String type, String memory, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        if (type.equals("owner")) {
            return new AbstractEffectAppender() {
                @Override
                protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                    return new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            actionContext.setValueToMemory(memory, actionContext.getSource().getOwner());
                        }
                    };
                }

                @Override
                public boolean isPlayableInFull(ActionContext actionContext) {
                    return true;
                }
            };
        } else if (type.startsWith("owner(") && type.endsWith(")")) {
            String filter = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);
            return new AbstractEffectAppender() {
                @Override
                public boolean isPlayableInFull(ActionContext actionContext) {
                    return true;
                }

                @Override
                protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                    return new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            final Filterable filterable = filterableSource.getFilterable(actionContext);
                            if (filterable instanceof PhysicalCard) {
                                actionContext.setValueToMemory(memory, ((PhysicalCard) filterable).getOwner());
                            } else {
                                final Collection<PhysicalCard> physicalCards = Filters.filterActive(game, filterable);
                                PhysicalCard card = physicalCards.iterator().next();
                                actionContext.setValueToMemory(memory, card.getOwner());
                            }
                        }
                    };
                }
            };
        }
        throw new InvalidCardDefinitionException("Unable to resolve player resolver of type: " + type);
    }
}
