package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.game.DefaultGame;

public interface PlayerSource {
    String getPlayer(DefaultActionContext<DefaultGame> actionContext);
}
