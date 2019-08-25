package com.gempukku.lotro.cards.build.field;

import com.gempukku.lotro.cards.build.BuiltLotroCardBlueprint;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.FieldProcessor;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.effect.ActivatedEffectProcessor;
import com.gempukku.lotro.cards.build.field.effect.ActivatedTriggerEffectProcessor;
import com.gempukku.lotro.cards.build.field.effect.EventEffectProcessor;
import com.gempukku.lotro.cards.build.field.effect.TriggerEffectProcessor;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EffectFieldProcessor implements FieldProcessor {
    private Map<String, EffectProcessor> effectProcessors = new HashMap<>();

    public EffectFieldProcessor() {
        effectProcessors.put("trigger", new TriggerEffectProcessor());
        effectProcessors.put("activatedtrigger", new ActivatedTriggerEffectProcessor());
        effectProcessors.put("activated", new ActivatedEffectProcessor());
        effectProcessors.put("event", new EventEffectProcessor());
    }

    @Override
    public void processField(String key, Object value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        final JSONObject[] effectsArray = FieldUtils.getObjectArray(value, key);
        for (JSONObject effect : effectsArray) {
            final String effectType = FieldUtils.getString(effect.get("type"), "type");
            final EffectProcessor effectProcessor = effectProcessors.get(effectType.toLowerCase());
            if (effectProcessor == null)
                throw new InvalidCardDefinitionException("Unable to find effect of type: " + effectType);
            effectProcessor.processEffect(effect, blueprint, environment);
        }
    }
}
