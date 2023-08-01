package com.gempukku.lotro.cards.build.field.effect.trigger;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.FilterableSource;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.effects.results.WoundResult;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.TriggerConditions;
import org.json.simple.JSONObject;

public class TakesWound implements TriggerCheckerProducer {
    @Override
    public TriggerChecker getTriggerChecker(JSONObject value, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "filter", "source", "memorize");

        String source = FieldUtils.getString(value.get("source"), "source", "any");
        String filter = FieldUtils.getString(value.get("filter"), "filter", "any");

        final String memorize = FieldUtils.getString(value.get("memorize"), "memorize");

        final FilterableSource sourceFilter = environment.getFilterFactory().generateFilter(source, environment);
        final FilterableSource targetFilterable = environment.getFilterFactory().generateFilter(filter, environment);

        return new TriggerChecker() {
            @Override
            public boolean isBefore() {
                return false;
            }

            @Override
            public boolean accepts(DefaultActionContext<DefaultGame> actionContext) {
                final Filterable filterable = targetFilterable.getFilterable(actionContext);
                final Filterable sourceFilterable = sourceFilter.getFilterable(actionContext);
                final boolean result = TriggerConditions.forEachWounded(actionContext.getGame(), actionContext.getEffectResult(), filterable);
                if(result && !source.equals("any")) {
                    var sources = ((WoundResult) actionContext.getEffectResult()).getSources();
                    if(sources.stream().noneMatch(x -> Filters.and(sourceFilterable).accepts(actionContext.getGame(), x))) {
                        return false;
                    }
                }
                if (result && memorize != null) {
                    final LotroPhysicalCard woundedCard = ((WoundResult) actionContext.getEffectResult()).getWoundedCard();
                    actionContext.setCardMemory(memorize, woundedCard);
                }
                return result;
            }
        };
    }
}
