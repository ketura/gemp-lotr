package com.gempukku.lotro.cards.build.field;

import com.gempukku.lotro.cards.BuiltLotroCardBlueprint;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.FieldProcessor;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.common.SitesBlock;

import java.util.Arrays;

public class AllyHomeFieldProcessor implements FieldProcessor {
    @Override
    public void processField(String key, Object value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        final String allyHome = FieldUtils.getString(value, "allyHome");
        final String[] allyHomeSplit = allyHome.split(",", 4);
        SitesBlock sitesBlock = Enum.valueOf(SitesBlock.class, allyHomeSplit[0].toUpperCase().replace(' ', '_'));

        String[] sites = Arrays.copyOfRange(allyHomeSplit, 1, allyHomeSplit.length);
        int[] numbers = Arrays.stream(sites).mapToInt(Integer::parseInt).toArray();

        blueprint.setAllyHomeSites(sitesBlock, numbers);
    }
}
