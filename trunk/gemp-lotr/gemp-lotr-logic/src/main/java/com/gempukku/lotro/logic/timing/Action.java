package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public interface Action {
    public PhysicalCard getActionSource();

    public Keyword getType();

    public String getText(LotroGame game);

    public Effect nextEffect();
}
