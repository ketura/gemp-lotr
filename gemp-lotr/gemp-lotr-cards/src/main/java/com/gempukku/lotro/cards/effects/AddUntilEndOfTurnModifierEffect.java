package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class AddUntilEndOfTurnModifierEffect extends UnrespondableEffect {
    private Modifier _modifier;

    public AddUntilEndOfTurnModifierEffect(Modifier modifier) {
        _modifier = modifier;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        game.getModifiersEnvironment().addUntilEndOfTurnModifier(_modifier);
    }
}
