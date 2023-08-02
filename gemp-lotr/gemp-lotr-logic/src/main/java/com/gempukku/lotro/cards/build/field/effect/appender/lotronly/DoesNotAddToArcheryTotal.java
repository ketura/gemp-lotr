package com.gempukku.lotro.cards.build.field.effect.appender.lotronly;

import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.ValueSource;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.appender.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.DelayedAppender;
import com.gempukku.lotro.cards.build.field.effect.appender.MultiEffectAppender;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.CardResolver;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.effects.Effect;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.modifiers.lotronly.DoesNotAddToArcheryTotalModifier;
import org.json.simple.JSONObject;

import java.util.Collection;

public class DoesNotAddToArcheryTotal implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "count", "filter", "memorize");

        final ValueSource valueSource = ValueResolver.resolveEvaluator(effectObject.get("count"), 1, environment);
        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter");
        final String memory = FieldUtils.getString(effectObject.get("memorize"), "memorize", "_temp");

        MultiEffectAppender result = new MultiEffectAppender();

        result.addEffectAppender(
                CardResolver.resolveCards(filter,
                        valueSource, memory, "you", "Choose cards to make not add to archery total", environment));
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Effect createEffect(boolean cost, CostToEffectAction action, DefaultActionContext actionContext) {
                        final Collection<? extends LotroPhysicalCard> cardsFromMemory = actionContext.getCardsFromMemory(memory);
                        return new AddUntilEndOfPhaseModifierEffect(
                                new DoesNotAddToArcheryTotalModifier(actionContext.getSource(), Filters.in(cardsFromMemory)));
                    }
                });

        return result;
    }

}
