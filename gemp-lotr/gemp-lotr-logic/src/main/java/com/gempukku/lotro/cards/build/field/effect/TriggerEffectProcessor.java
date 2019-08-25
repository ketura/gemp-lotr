package com.gempukku.lotro.cards.build.field.effect;

import com.gempukku.lotro.cards.build.BuiltLotroCardBlueprint;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.EffectProcessor;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.trigger.TriggerActionSource;
import com.gempukku.lotro.cards.build.field.effect.trigger.TriggerChecker;
import org.json.simple.JSONObject;

public class TriggerEffectProcessor implements EffectProcessor {
    @Override
    public void processEffect(JSONObject value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        final JSONObject[] triggerArray = FieldUtils.getObjectArray(value.get("trigger"), "trigger");
        final boolean optional = FieldUtils.getBoolean(value.get("optional"), "optional", false);

        for (JSONObject trigger : triggerArray) {
            final TriggerChecker triggerChecker = environment.getTriggerCheckerFactory().getTriggerChecker(trigger, environment);
            final boolean before = triggerChecker.isBefore();

            TriggerActionSource triggerActionSource = EffectUtils.getTriggerActionSource(value, triggerChecker, environment);

            if (before) {
                if (optional)
                    blueprint.appendOptionalBeforeTrigger(triggerActionSource);
                else
                    blueprint.appendRequiredBeforeTrigger(triggerActionSource);
            } else {
                if (optional)
                    blueprint.appendOptionalAfterTrigger(triggerActionSource);
                else
                    blueprint.appendRequiredAfterTrigger(triggerActionSource);
            }
        }
    }
}
