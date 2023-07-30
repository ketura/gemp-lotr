package com.gempukku.lotro.effects;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.Modifier;

public class AddUntilEndOfTurnModifierEffect extends UnrespondableEffect {
    private final Modifier _modifier;

    public AddUntilEndOfTurnModifierEffect(Modifier modifier) {
        _modifier = modifier;
    }

    @Override
    public void doPlayEffect(DefaultGame game) {
        game.getModifiersEnvironment().addUntilEndOfTurnModifier(_modifier);
    }
}
