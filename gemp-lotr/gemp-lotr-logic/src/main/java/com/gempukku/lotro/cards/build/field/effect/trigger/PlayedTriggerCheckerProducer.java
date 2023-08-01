package com.gempukku.lotro.cards.build.field.effect.trigger;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.FilterableSource;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.effects.results.PlayCardResult;
import com.gempukku.lotro.effects.results.PlayEventResult;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.TriggerConditions;
import org.json.simple.JSONObject;

public class PlayedTriggerCheckerProducer implements TriggerCheckerProducer {
    @Override
    public TriggerChecker getTriggerChecker(JSONObject value, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "filter", "on", "memorize", "exertsRanger");

        final String filterString = FieldUtils.getString(value.get("filter"), "filter");
        final String onString = FieldUtils.getString(value.get("on"), "on");
        final String memorize = FieldUtils.getString(value.get("memorize"), "memorize");
        boolean exertsRanger = FieldUtils.getBoolean(value.get("exertsRanger"), "exertsRanger", false);
        final FilterableSource filter = environment.getFilterFactory().generateFilter(filterString, environment);
        final FilterableSource onFilter = (onString != null) ? environment.getFilterFactory().generateFilter(onString, environment) : null;
        return new TriggerChecker() {
            @Override
            public boolean accepts(DefaultActionContext<DefaultGame> actionContext) {
                final Filterable filterable = filter.getFilterable(actionContext);
                boolean played;
                if (onFilter != null) {
                    final Filterable onFilterable = onFilter.getFilterable(actionContext);
                    played = TriggerConditions.playedOn(actionContext.getGame(), actionContext.getEffectResult(), onFilterable, filterable);
                } else {
                    played = TriggerConditions.played(actionContext.getGame(), actionContext.getEffectResult(), filterable);
                }

                if (played) {
                    PlayCardResult playCardResult = (PlayCardResult) actionContext.getEffectResult();
                    if (exertsRanger && playCardResult instanceof PlayEventResult && !((PlayEventResult) playCardResult).isRequiresRanger())
                        return false;

                    if (memorize != null) {
                        LotroPhysicalCard playedCard = playCardResult.getPlayedCard();
                        actionContext.setCardMemory(memorize, playedCard);
                    }
                }
                return played;
            }

            @Override
            public boolean isBefore() {
                return false;
            }
        };
    }
}
