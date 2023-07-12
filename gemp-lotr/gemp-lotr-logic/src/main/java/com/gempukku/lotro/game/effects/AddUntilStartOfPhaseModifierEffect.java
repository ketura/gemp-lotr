package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.Modifier;

public class AddUntilStartOfPhaseModifierEffect extends UnrespondableEffect {
    private final Modifier _modifier;
    private final Phase _phase;

    public AddUntilStartOfPhaseModifierEffect(Modifier modifier, Phase phase) {
        _modifier = modifier;
        _phase = phase;
    }

    @Override
    public void doPlayEffect(DefaultGame game) {
        game.getModifiersEnvironment().addUntilStartOfPhaseModifier(_modifier, _phase);
    }
}
