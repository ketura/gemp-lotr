package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class AddUntilEndOfPhaseModifierEffect extends UnrespondableEffect {
    private final Modifier _modifier;
    private final Phase _phase;

    public AddUntilEndOfPhaseModifierEffect(Modifier modifier) {
        this(modifier, null);
    }

    public AddUntilEndOfPhaseModifierEffect(Modifier modifier, Phase phase) {
        _modifier = modifier;
        _phase = phase;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        Phase phase = _phase;
        if (phase == null)
            phase = game.getGameState().getCurrentPhase();
        game.getModifiersEnvironment().addUntilEndOfPhaseModifier(_modifier, phase);
    }
}
