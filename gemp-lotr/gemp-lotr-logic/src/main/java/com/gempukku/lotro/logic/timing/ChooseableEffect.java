package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.state.LotroGame;

public interface ChooseableEffect extends Effect {
    public boolean canPlayEffect(LotroGame game);
}
