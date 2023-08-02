package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.CardResolver;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.TimeResolver;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.effects.AddUntilModifierEffect;
import com.gempukku.lotro.effects.Effect;
import com.gempukku.lotro.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.modifiers.lotronly.KeywordModifier;
import com.gempukku.lotro.rules.GameUtils;
import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class AddKeyword implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "count", "filter", "memorize", "keyword", "amount", "until");

        final ValueSource valueSource = ValueResolver.resolveEvaluator(effectObject.get("count"), 1, environment);
        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter");
        final String memory = FieldUtils.getString(effectObject.get("memorize"), "memorize", "_temp");
        final String keywordString = FieldUtils.getString(effectObject.get("keyword"), "keyword");
        final TimeResolver.Time until = TimeResolver.resolveTime(effectObject.get("until"), "end(current)");

        Function<DefaultActionContext, Keyword> keywordFunction;
        ValueSource amount;
        if (keywordString.startsWith("fromMemory(") && keywordString.endsWith(")")) {
            String keywordMemory = keywordString.substring(keywordString.indexOf("(") + 1, keywordString.lastIndexOf(")"));
            keywordFunction = actionContext -> Keyword.valueOf(actionContext.getValueFromMemory(keywordMemory));
            amount = new ConstantEvaluator(1);
        } else {
            final String[] keywordSplit = keywordString.split("\\+");
            Keyword keyword = FieldUtils.getEnum(Keyword.class, keywordSplit[0], "keyword");
            keywordFunction = (actionContext) -> keyword;
            int value = 1;
            if (keywordSplit.length == 2)
                value = Integer.parseInt(keywordSplit[1]);

            amount = ValueResolver.resolveEvaluator(effectObject.get("amount"), value, environment);
        }

        MultiEffectAppender result = new MultiEffectAppender();

        result.addEffectAppender(
                CardResolver.resolveCards(filter, valueSource, memory, "you", "Choose cards to add keyword to", environment));
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected List<? extends Effect> createEffects(boolean cost, CostToEffectAction action, DefaultActionContext actionContext) {
                        List<Effect> result = new LinkedList<>();
                        final Collection<? extends LotroPhysicalCard> cardsFromMemory = actionContext.getCardsFromMemory(memory);
                        for (LotroPhysicalCard physicalCard : cardsFromMemory) {
                            final int keywordCount = amount.getEvaluator(actionContext).evaluateExpression(actionContext.getGame(), physicalCard);
                            result.add(new AddUntilModifierEffect(
                                    new KeywordModifier(actionContext.getSource(), physicalCard, keywordFunction.apply(actionContext), keywordCount), until));
                        }

                        actionContext.getGame().getGameState().sendMessage(
                                GameUtils.getCardLink(actionContext.getSource())
                                        + " adds " + keywordFunction.apply(actionContext).getHumanReadableGeneric()
                                        + " to " + GameUtils.getAppendedNames(cardsFromMemory)
                                        + " until " + until.getHumanReadable());
                        return result;
                    }
                });

        return result;
    }

}
