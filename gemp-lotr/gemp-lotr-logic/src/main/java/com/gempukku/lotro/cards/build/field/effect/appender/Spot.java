package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.CardResolver;
import org.json.simple.JSONObject;

public class Spot implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "filter", "memorize", "text");

        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter");
        final String memorize = FieldUtils.getString(effectObject.get("memorize"), "memorize", "_temp");
        final String text = FieldUtils.getString(effectObject.get("text"), "text", "Choose card to spot");

        return CardResolver.resolveCard(
                filter, memorize, "you", text, environment);
    }
}
