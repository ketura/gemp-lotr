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
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.ModifierFlag;
import com.gempukku.lotro.modifiers.evaluator.ConstantEvaluator;
import org.json.simple.JSONObject;

import java.util.Collection;

public class PreventWound implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "filter", "memorize");

        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter", "all(any)");
        final String memory = FieldUtils.getString(effectObject.get("memorize"), "memorize", "_temp");

        MultiEffectAppender result = new MultiEffectAppender();

        result.addEffectAppender(
                CardResolver.resolveCards(filter,
                        (actionContext) -> {
                            final PreventableCardEffect preventableEffect = (PreventableCardEffect) actionContext.getEffect();
                            return Filters.in(preventableEffect.getAffectedCardsMinusPrevented(actionContext.getGame()));
                        }, new ConstantEvaluator(1), memory, "you", "Choose characters to prevent effect to", environment));
        result.addEffectAppender(
                new DelayedAppender<>() {
                    @Override
                    protected Effect createEffect(boolean cost, CostToEffectAction action, DefaultActionContext actionContext) {
                        final Collection<? extends LotroPhysicalCard> cards = actionContext.getCardsFromMemory(memory);
                        final PreventableCardEffect preventableEffect = (PreventableCardEffect) actionContext.getEffect();

                        return new PreventCardEffect(preventableEffect, Filters.in(cards));
                    }

                    @Override
                    public boolean isPlayableInFull(DefaultActionContext<DefaultGame> actionContext) {
                        final DefaultGame game = actionContext.getGame();
                        return !game.getModifiersQuerying().hasFlagActive(game, ModifierFlag.CANT_PREVENT_WOUNDS);
                    }
                });

        return result;
    }

}
