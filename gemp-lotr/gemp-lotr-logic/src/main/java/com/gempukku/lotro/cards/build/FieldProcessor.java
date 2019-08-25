package com.gempukku.lotro.cards.build;

public interface FieldProcessor {
    void processField(String key, Object value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException;
}
