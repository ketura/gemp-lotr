package com.gempukku.lotro.cards.build.field;

import com.gempukku.lotro.cards.build.BuiltLotroCardBlueprint;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.FieldProcessor;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;

public class CultureFieldProcessor implements FieldProcessor {
    @Override
    public void processField(String key, Object value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        final Culture culture = FieldUtils.getEnum(Culture.class, value, key);
        blueprint.setCulture(culture);

        if (culture != Culture.GOLLUM)
            blueprint.setSide(culture.isFP() ? Side.FREE_PEOPLE : Side.SHADOW);
    }
}
