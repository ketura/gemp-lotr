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
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.RemoveTokenEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.timing.Effect;
import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class RemoveTokens implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "count", "culture", "filter", "memorize");

        final ValueSource valueSource = ValueResolver.resolveEvaluator(effectObject.get("count"), 1, environment);
        final Culture culture = FieldUtils.getEnum(Culture.class, effectObject.get("culture"), "culture");
        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter", "self");
        final String memory = FieldUtils.getString(effectObject.get("memorize"), "memorize", "_temp");

        final Token token = Token.findTokenForCulture(culture);

        MultiEffectAppender result = new MultiEffectAppender();

        result.addEffectAppender(
                CardResolver.resolveCards(filter,
                        actionContext -> {
                            final int tokenCount = valueSource.getEvaluator(actionContext).evaluateExpression(actionContext.getGame(), null);
                            return (Filter) (game, physicalCard) -> game.getGameState().getTokenCount(physicalCard, token) >= tokenCount;
                        },
                        new ConstantEvaluator(1), memory, "you", "Choose card to remove tokens from", environment));
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected List<Effect> createEffects(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                        final Collection<? extends PhysicalCard> cardsFromMemory = actionContext.getCardsFromMemory(memory);

                        final int tokenCount = valueSource.getEvaluator(actionContext).evaluateExpression(actionContext.getGame(), null);

                        List<Effect> result = new LinkedList<>();
                        for (PhysicalCard card : cardsFromMemory)
                            result.add(new RemoveTokenEffect(actionContext.getSource(), card, token, tokenCount));

                        return result;
                    }
                });

        return result;
    }

}
