package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;

import java.util.Collection;

public abstract class AbstractSuccessfulEffect implements Effect {
    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }

    @Override
    public boolean wasCarriedOut() {
        return true;
    }

    protected final String getAppendedTextNames(Collection<PhysicalCard> cards) {
        return GameUtils.getAppendedTextNames(cards);
    }

    protected final String getAppendedNames(Collection<PhysicalCard> cards) {
        return GameUtils.getAppendedNames(cards);
    }
}
