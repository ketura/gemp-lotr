package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class AddUntilStartOfPhaseModifierEffect extends UnrespondableEffect {
    private Modifier _modifier;
    private Phase _phase;

    public AddUntilStartOfPhaseModifierEffect(Modifier modifier, Phase phase) {
        _modifier = modifier;
        _phase = phase;
    }

    @Override
    public void playEffect(LotroGame game) {
        game.getModifiersEnvironment().addUntilStartOfPhaseModifier(_modifier, _phase);
    }
}
