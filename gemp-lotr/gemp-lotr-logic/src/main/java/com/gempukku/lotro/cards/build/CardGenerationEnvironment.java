package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.cards.build.field.effect.EffectAppenderFactory;
import com.gempukku.lotro.cards.build.field.effect.filter.FilterFactory;
import com.gempukku.lotro.cards.build.field.effect.requirement.RequirementFactory;
import com.gempukku.lotro.cards.build.field.effect.trigger.TriggerCheckerFactory;

public interface CardGenerationEnvironment {
    EffectAppenderFactory getEffectAppenderFactory();

    FilterFactory getFilterFactory();

    RequirementFactory getRequirementFactory();

    TriggerCheckerFactory getTriggerCheckerFactory();
}
