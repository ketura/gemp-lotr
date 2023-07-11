package com.gempukku.lotro.game.timing;

import com.gempukku.lotro.game.DefaultGame;

public interface Preventable {
    public void prevent();

    public boolean isPrevented(DefaultGame game);
}
