package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;

public interface Action {
    public PhysicalCard getActionSource();

    public Keyword getType();

    public String getText();

    public Effect nextEffect();
}
