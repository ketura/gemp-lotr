package com.gempukku.lotro.cards.build.field.effect.appender.lotronly;

import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.appender.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.DelayedAppender;
import com.gempukku.lotro.cards.build.field.effect.appender.MultiEffectAppender;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.CardResolver;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.effects.Effect;
import com.gempukku.lotro.effects.NegateWoundEffect;
import com.gempukku.lotro.effects.WoundCharactersEffect;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.evaluator.ConstantEvaluator;
import org.json.simple.JSONObject;

import java.util.Collection;

public class NegateWound implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "filter");

        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter", "all(any)");

        MultiEffectAppender result = new MultiEffectAppender();

        result.addEffectAppender(
                CardResolver.resolveCards(filter,
                        (actionContext) -> {
                            final WoundCharactersEffect woundEffect = (WoundCharactersEffect) actionContext.getEffect();
                            return Filters.in(woundEffect.getAffectedCardsMinusPrevented(actionContext.getGame()));
                        }, new ConstantEvaluator(1), "_temp", "you", "Choose characters to negate wound to", environment));
        result.addEffectAppender(
                new DelayedAppender<>() {
                    @Override
                    protected Effect createEffect(boolean cost, CostToEffectAction action, DefaultActionContext actionContext) {
                        final Collection<? extends LotroPhysicalCard> cards = actionContext.getCardsFromMemory("_temp");
                        final WoundCharactersEffect woundEffect = (WoundCharactersEffect) actionContext.getEffect();

                        return new NegateWoundEffect(woundEffect, Filters.in(cards));
                    }

                    @Override
                    public boolean isPlayableInFull(DefaultActionContext<DefaultGame> actionContext) {
                        return true;
                    }
                });

        return result;
    }

}
