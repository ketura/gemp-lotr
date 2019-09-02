package com.gempukku.lotro.cards.build.field.effect.filter;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.FilterableSource;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;

public interface FilterableSourceProducer {
    FilterableSource createFilterableSource(String parameter, CardGenerationEnvironment environment) throws InvalidCardDefinitionException;
}
