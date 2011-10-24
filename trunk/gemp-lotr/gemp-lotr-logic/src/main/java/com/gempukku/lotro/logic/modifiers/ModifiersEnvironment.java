package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;

public interface ModifiersEnvironment {
    public LimitCounter getUntilEndOfPhaseLimitCounter(PhysicalCard card, Phase phase);

    public ModifierHook addAlwaysOnModifier(Modifier modifier);

    public void addUntilStartOfPhaseModifier(Modifier modifier, Phase phase);

    public void addUntilEndOfPhaseModifier(Modifier modifier, Phase phase);

    public void addUntilEndOfTurnModifier(Modifier modifier);

    public void addedWound(PhysicalCard card);
}
