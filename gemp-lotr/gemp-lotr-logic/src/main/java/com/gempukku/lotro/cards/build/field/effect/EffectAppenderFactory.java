package com.gempukku.lotro.cards.build.field.effect;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.*;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EffectAppenderFactory {
    private Map<String, EffectAppenderProducer> effectAppenderProducers = new HashMap<>();

    public EffectAppenderFactory() {
        effectAppenderProducers.put("stacktopcardofdrawdeck", new StackTopCardOfDrawDeck());
        effectAppenderProducers.put("discardfromplay", new DiscardFromPlay());
        effectAppenderProducers.put("preventdiscardfromplay", new PreventCardEffectAppender());
        effectAppenderProducers.put("exert", new Exert());
        effectAppenderProducers.put("addstrength", new AddStrength());
        effectAppenderProducers.put("discardtopcardfromdeck", new DiscardTopCardFromDeck());
        effectAppenderProducers.put("addtwilight", new AddTwilight());
    }

    public EffectAppender getEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        final String type = FieldUtils.getString(effectObject.get("type"), "type");
        final EffectAppenderProducer effectAppenderProducer = effectAppenderProducers.get(type.toLowerCase());
        if (effectAppenderProducer == null)
            throw new InvalidCardDefinitionException("Unable to find effect of type: " + type);
        return effectAppenderProducer.createEffectAppender(effectObject, environment);
    }
}
