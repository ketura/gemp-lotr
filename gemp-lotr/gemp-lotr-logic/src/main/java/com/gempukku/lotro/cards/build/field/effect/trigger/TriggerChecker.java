package com.gempukku.lotro.cards.build.field.effect.trigger;

import com.gempukku.lotro.cards.build.Requirement;

public interface TriggerChecker extends Requirement {
    boolean isBefore();
}
