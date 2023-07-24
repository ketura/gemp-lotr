package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Phase;

public interface ModifiersEnvironment {
    public ModifierHook addAlwaysOnModifier(Modifier modifier);

    public void addUntilStartOfPhaseModifier(Modifier modifier, Phase phase);

    public void addUntilEndOfPhaseModifier(Modifier modifier, Phase phase);

    public void addUntilEndOfTurnModifier(Modifier modifier);

    public void addedWound(LotroPhysicalCard card);

    public int getWoundsTakenInCurrentPhase(LotroPhysicalCard card);
}
