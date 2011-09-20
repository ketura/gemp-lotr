package com.gempukku.lotro.logic.timing.actions;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

public abstract class SystemAction implements Action {
    @Override
    public PhysicalCard getActionSource() {
        throw new UnsupportedOperationException("System action has no action source");
    }

    @Override
    public Keyword getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        throw new UnsupportedOperationException("System action has no text");
    }
}
