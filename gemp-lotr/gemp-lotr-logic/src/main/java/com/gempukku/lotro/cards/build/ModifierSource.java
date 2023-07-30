package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.modifiers.Modifier;

public interface ModifierSource {
    Modifier getModifier(ActionContext actionContext);
}
