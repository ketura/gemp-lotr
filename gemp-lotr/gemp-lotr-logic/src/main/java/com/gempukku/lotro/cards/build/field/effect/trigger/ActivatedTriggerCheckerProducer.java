package com.gempukku.lotro.cards.build.field.effect.trigger;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.FilterableSource;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;
import com.gempukku.lotro.logic.timing.results.PlayEventResult;
import org.json.simple.JSONObject;

public class ActivatedTriggerCheckerProducer implements TriggerCheckerProducer {
    @Override
    public TriggerChecker getTriggerChecker(JSONObject value, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "filter", "memorize");

        final String filterString = FieldUtils.getString(value.get("filter"), "filter");
        final String memorize = FieldUtils.getString(value.get("memorize"), "memorize");
        final FilterableSource filter = environment.getFilterFactory().generateFilter(filterString, environment);
        return new TriggerChecker() {
            @Override
            public boolean accepts(ActionContext actionContext) {
                final Filterable filterable = filter.getFilterable(actionContext);
                boolean activated = TriggerConditions.activated(actionContext.getGame(), actionContext.getEffectResult(), filterable);

                if (activated) {
                    PlayCardResult playCardResult = (PlayCardResult) actionContext.getEffectResult();

                    if (memorize != null) {
                        PhysicalCard playedCard = playCardResult.getPlayedCard();
                        actionContext.setCardMemory(memorize, playedCard);
                    }
                }
                return activated;
            }

            @Override
            public boolean isBefore() {
                return false;
            }
        };
    }
}
