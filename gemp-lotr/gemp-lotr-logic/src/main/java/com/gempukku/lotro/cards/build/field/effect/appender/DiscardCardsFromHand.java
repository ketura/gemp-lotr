package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.ValueSource;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.CardResolver;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.Effect;
import org.json.simple.JSONObject;

import java.util.Collection;

public class DiscardCardsFromHand implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "forced", "count", "filter");

        final boolean forced = FieldUtils.getBoolean(effectObject.get("forced"), "forced");
        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter", "any");

        final ValueSource countSource = ValueResolver.resolveEvaluator(effectObject.get("count"), 1, environment);

        MultiEffectAppender result = new MultiEffectAppender();

        result.addEffectAppender(
                CardResolver.resolveCardsInHand(filter, countSource, "_temp", "owner", "Choose cards to discard", environment));
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                        final Collection<? extends PhysicalCard> cardsToDiscard = actionContext.getCardsFromMemory("_temp");
                        return new DiscardCardsFromHandEffect(actionContext.getSource(), actionContext.getPerformingPlayer(), cardsToDiscard, forced);
                    }

                    @Override
                    public boolean isPlayableInFull(ActionContext actionContext) {
                        return (!forced || actionContext.getGame().getModifiersQuerying().canDiscardCardsFromHand(actionContext.getGame(), actionContext.getPerformingPlayer(), actionContext.getSource()));
                    }
                });

        return result;
    }

}
