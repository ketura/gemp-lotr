package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.CardResolver;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.effects.DiscardStackedCardsEffect;
import com.gempukku.lotro.effects.Effect;
import org.json.simple.JSONObject;

public class DiscardStackedCards implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "on", "filter", "count" ,"memorize");

        String on = FieldUtils.getString(effectObject.get("on"), "on", "any");
        String filter = FieldUtils.getString(effectObject.get("filter"), "filter", "choose(any)");
        final ValueSource valueSource = ValueResolver.resolveEvaluator(effectObject.get("count"), 1, environment);
        final String memory = FieldUtils.getString(effectObject.get("memorize"), "memorize", "_temp");

        final FilterableSource onFilterSource = environment.getFilterFactory().generateFilter(on, environment);

        MultiEffectAppender result = new MultiEffectAppender();
        result.addEffectAppender(
                CardResolver.resolveStackedCards(filter, valueSource, onFilterSource, memory, "you", "Choose stacked cards to discard", environment));
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Effect createEffect(boolean cost, CostToEffectAction action, DefaultActionContext actionContext) {
                        return new DiscardStackedCardsEffect(actionContext.getSource(), actionContext.getCardsFromMemory(memory));
                    }
                });
        return result;
    }

}
