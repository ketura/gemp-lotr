package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class AddUntilEndOfPhaseModifierEffect extends UnrespondableEffect {
    private Modifier _modifier;
    private Phase _phase;

    public AddUntilEndOfPhaseModifierEffect(Modifier modifier, Phase phase) {
        _modifier = modifier;
        _phase = phase;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        game.getModifiersEnvironment().addUntilEndOfPhaseModifier(_modifier, _phase);
    }
}
