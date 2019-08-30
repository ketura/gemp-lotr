package com.gempukku.lotro.cards.build.field;

import com.gempukku.lotro.cards.build.BuiltLotroCardBlueprint;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.FieldProcessor;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;

public class TitleFieldProcessor implements FieldProcessor {
    @Override
    public void processField(String key, Object value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        boolean unique = false;
        String title = FieldUtils.getString(value, key);
        if (title.startsWith("*")) {
            unique = true;
            title = title.substring(1);
        }
        if (unique)
            blueprint.setUnique(true);
        blueprint.setTitle(title);
    }
}
