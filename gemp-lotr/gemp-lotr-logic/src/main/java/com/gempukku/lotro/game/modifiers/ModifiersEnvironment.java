package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.cards.PhysicalCard;

public interface ModifiersEnvironment {
    public ModifierHook addAlwaysOnModifier(Modifier modifier);

    public void addUntilStartOfPhaseModifier(Modifier modifier, Phase phase);

    public void addUntilEndOfPhaseModifier(Modifier modifier, Phase phase);

    public void addUntilEndOfTurnModifier(Modifier modifier);

    public void addedWound(PhysicalCard card);

    public int getWoundsTakenInCurrentPhase(PhysicalCard card);
}
