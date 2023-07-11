package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.Modifier;
import com.gempukku.lotro.game.timing.UnrespondableEffect;

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
