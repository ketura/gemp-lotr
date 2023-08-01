package com.gempukku.lotro.cards.build.field.effect.appender.lotronly;

import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.DelayedAppender;
import com.gempukku.lotro.cards.build.field.effect.appender.MultiEffectAppender;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.CardResolver;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.effects.Effect;
import com.gempukku.lotro.effects.PreventCardEffect;
import com.gempukku.lotro.effects.PreventableCardEffect;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.modifiers.evaluator.ConstantEvaluator;
import org.json.simple.JSONObject;

import java.util.Collection;

public class PreventExert implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "filter");

        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter", "all(any)");

        MultiEffectAppender result = new MultiEffectAppender();

        result.addEffectAppender(
                CardResolver.resolveCards(filter,
                        (actionContext) -> {
                            final PreventableCardEffect preventableEffect = (PreventableCardEffect) actionContext.getEffect();
                            return Filters.in(preventableEffect.getAffectedCardsMinusPrevented(actionContext.getGame()));
                        }, new ConstantEvaluator(1), "_temp", "you", "Choose characters to prevent effect to", environment));
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Effect createEffect(boolean cost, CostToEffectAction action, DefaultActionContext actionContext) {
                        final Collection<? extends LotroPhysicalCard> cards = actionContext.getCardsFromMemory("_temp");
                        final PreventableCardEffect preventableEffect = (PreventableCardEffect) actionContext.getEffect();

                        return new PreventCardEffect(preventableEffect, Filters.in(cards));
                    }
                });

        return result;
    }

}
