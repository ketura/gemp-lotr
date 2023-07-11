package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.game.modifiers.Modifier;

public interface ModifierSource {
    Modifier getModifier(ActionContext actionContext);
}
