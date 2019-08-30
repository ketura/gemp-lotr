package com.gempukku.lotro.cards.build.field.effect.trigger;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.FilterableSource;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import org.json.simple.JSONObject;

public class AboutToTakeWound implements TriggerCheckerProducer {
    @Override
    public TriggerChecker getTriggerChecker(JSONObject value, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "source", "filter");

        String source = FieldUtils.getString(value.get("source"), "source", "any");
        String filter = FieldUtils.getString(value.get("filter"), "source");

        final FilterableSource sourceFilter = environment.getFilterFactory().generateFilter(source);
        final FilterableSource affectedFilter = environment.getFilterFactory().generateFilter(filter);

        return new TriggerChecker() {
            @Override
            public boolean accepts(ActionContext actionContext, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                final Filterable sourceFilterable = sourceFilter.getFilterable(actionContext, playerId, game, self, effectResult, effect);
                final Filterable affected = affectedFilter.getFilterable(actionContext, playerId, game, self, effectResult, effect);
                if (sourceFilterable == Filters.any)
                    return TriggerConditions.isGettingWounded(effect, game, affected);
                else
                    return TriggerConditions.isGettingWoundedBy(effect, game, sourceFilterable, affected);
            }

            @Override
            public boolean isBefore() {
                return true;
            }
        };
    }
}
