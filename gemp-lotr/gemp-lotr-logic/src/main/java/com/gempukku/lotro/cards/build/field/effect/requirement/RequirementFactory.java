package com.gempukku.lotro.cards.build.field.effect.requirement;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.PlayRequirement;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequirementFactory {
    private Map<String, RequirementProducer> requirementProducers = new HashMap<>();

    public RequirementFactory() {
        requirementProducers.put("candiscardfromplay", new CanDiscardFromPlay());
        requirementProducers.put("canexert", new CanExert());
        requirementProducers.put("canspot", new CanSpot());
    }

    public PlayRequirement getRequirement(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        final String type = FieldUtils.getString(object.get("type"), "type");
        final RequirementProducer requirementProducer = requirementProducers.get(type.toLowerCase());
        if (requirementProducer == null)
            throw new InvalidCardDefinitionException("Unable to resolve requirement of type: " + type);
        return requirementProducer.getPlayRequirement(object, environment);
    }
}
