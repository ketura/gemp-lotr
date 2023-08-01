package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.game.DefaultGame;

public interface Requirement {
    boolean accepts(DefaultActionContext<DefaultGame> actionContext);
}
