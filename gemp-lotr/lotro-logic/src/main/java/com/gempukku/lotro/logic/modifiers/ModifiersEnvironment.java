package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Phase;

public interface ModifiersEnvironment {
    public ModifierHook addAlwaysOnModifier(Modifier modifier);

    public void addUntilStartOfPhaseModifier(Modifier modifier, Phase phase);

    public void addUntilEndOfPhaseModifier(Modifier modifier, Phase phase);

    public void addUntilEndOfTurnModifier(Modifier modifier);
}
