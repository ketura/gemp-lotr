package com.gempukku.lotro.cards.build.field.effect.trigger;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.FilterableSource;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import org.json.simple.JSONObject;

public class AboutToDiscardFromPlay implements TriggerCheckerProducer {
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
                return TriggerConditions.isGettingDiscardedBy(effect, game,
                        sourceFilter.getFilterable(actionContext, playerId, game, self, effectResult, effect),
                        affectedFilter.getFilterable(actionContext, playerId, game, self, effectResult, effect));
            }

            @Override
            public boolean isBefore() {
                return true;
            }
        };
    }
}
