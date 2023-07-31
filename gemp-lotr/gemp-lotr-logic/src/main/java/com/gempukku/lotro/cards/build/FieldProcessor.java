package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.cards.BuiltLotroCardBlueprint;

public interface FieldProcessor {
    void processField(String key, Object value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException;
}
