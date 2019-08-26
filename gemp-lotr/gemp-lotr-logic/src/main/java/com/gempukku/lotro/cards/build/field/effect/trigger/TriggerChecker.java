package com.gempukku.lotro.cards.build.field.effect.trigger;

import com.gempukku.lotro.cards.build.PlayRequirement;

public interface TriggerChecker extends PlayRequirement {
    boolean isBefore();
}
