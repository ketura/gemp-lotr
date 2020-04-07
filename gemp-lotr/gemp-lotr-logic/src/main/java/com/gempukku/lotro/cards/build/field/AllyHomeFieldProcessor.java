package com.gempukku.lotro.cards.build.field;

import com.gempukku.lotro.cards.build.BuiltLotroCardBlueprint;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.FieldProcessor;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.common.SitesBlock;

public class AllyHomeFieldProcessor implements FieldProcessor {
    @Override
    public void processField(String key, Object value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        final String allyHome = FieldUtils.getString(value, "allyHome");
        final String[] allyHomeSplit = allyHome.split(",", 2);
        SitesBlock sitesBlock = Enum.valueOf(SitesBlock.class, allyHomeSplit[0].toUpperCase().replace(' ', '_'));
        final int number = Integer.parseInt(allyHomeSplit[1]);

        blueprint.setAllyHomeSites(sitesBlock, new int[]{number});
    }
}
