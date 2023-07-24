package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.FilterableSource;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.game.effects.PlayNextSiteEffect;
import com.gempukku.lotro.game.effects.Effect;
import org.json.simple.JSONObject;

public class PlayNextSite implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "filter", "memorize");
        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter", "any");
        final String memorize = FieldUtils.getString(effectObject.get("memorize"), "memorize");

        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);

        return new DelayedAppender() {
            @Override
            public boolean isPlayableInFull(ActionContext actionContext) {
                final DefaultGame game = actionContext.getGame();
                final int nextSiteNumber = game.getGameState().getCurrentSiteNumber() + 1;
                final LotroPhysicalCard nextSite = game.getGameState().getSite(nextSiteNumber);
                final Filterable filterable = filterableSource.getFilterable(actionContext);
                final String playerId = actionContext.getPerformingPlayer();

                if (nextSiteNumber > 9 || nextSiteNumber < 1)
                    return false;

                if (nextSite != null && !game.getModifiersQuerying().canReplaceSite(game, actionContext.getPerformingPlayer(), nextSite))
                    return false;

                if (game.getFormat().isOrderedSites()) {
                    Filter printedSiteNumber = new Filter() {
                        @Override
                        public boolean accepts(DefaultGame game, LotroPhysicalCard physicalCard) {
                            return physicalCard.getBlueprint().getSiteNumber() == nextSiteNumber;
                        }
                    };
                    return Filters.filter(game.getGameState().getAdventureDeck(playerId), game, Filters.and(filterable, printedSiteNumber)).size() > 0;
                } else {
                    return Filters.filter(game.getGameState().getAdventureDeck(playerId), game, filterable).size() > 0;
                }
            }

            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                final Filterable filterable = filterableSource.getFilterable(actionContext);
                return new PlayNextSiteEffect(action, actionContext.getPerformingPlayer(), filterable) {
                    @Override
                    protected void sitePlayedCallback(LotroPhysicalCard site) {
                        if (memorize != null) {
                            actionContext.setCardMemory(memorize, site);
                        }
                    }
                };
            }
        };
    }
}
