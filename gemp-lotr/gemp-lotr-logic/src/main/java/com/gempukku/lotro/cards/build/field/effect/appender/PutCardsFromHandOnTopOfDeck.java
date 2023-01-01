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
import com.gempukku.lotro.logic.effects.PutCardFromHandOnTopOfDeckEffect;
import com.gempukku.lotro.logic.timing.Effect;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class PutCardsFromHandOnTopOfDeck implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "player", "hand", "optional", "filter", "count");

        final String player = FieldUtils.getString(effectObject.get("player"), "player", "you");
        final String hand = FieldUtils.getString(effectObject.get("hand"), "hand", "you");
        final boolean optional = FieldUtils.getBoolean(effectObject.get("optional"), "optional", false);
        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter", "choose(any)");
        final ValueSource count = ValueResolver.resolveEvaluator(effectObject.get("count"), 1, environment);

        var countObj = (JSONObject)effectObject.get("count");

        ValueSource valueSource;
        if (optional) {
            String countStr = "1";
            if(countObj != null && !Objects.equals(countObj.toJSONString().replaceAll("\s+", ""), "{}")) {
                countStr = countObj.toJSONString();
            }
            try {
                var obj = new JSONParser().parse("{ \"type\": \"range\", \"from\": 0, \"to\": " + countStr + " }");
                valueSource = ValueResolver.resolveEvaluator(obj, environment);
            } catch (ParseException e) {
                valueSource = ValueResolver.resolveEvaluator("0-1", environment);
            }
        }
        else {
            valueSource = count;
        }

        MultiEffectAppender result = new MultiEffectAppender();

        result.addEffectAppender(
                CardResolver.resolveCardsInHand(filter, valueSource, "_temp", player, hand, "Choose cards from hand", environment));
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected List<Effect> createEffects(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                        final Collection<? extends PhysicalCard> cards = actionContext.getCardsFromMemory("_temp");
                        List<Effect> result = new LinkedList<>();
                        for (PhysicalCard card : cards) {
                            result.add(new PutCardFromHandOnTopOfDeckEffect(card));
                        }
                        return result;
                    }
                });

        return result;
    }

}
