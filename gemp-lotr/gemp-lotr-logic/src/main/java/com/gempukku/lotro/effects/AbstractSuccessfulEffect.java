package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.GameUtils;

import java.util.Collection;

public abstract class AbstractSuccessfulEffect implements Effect {
    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return true;
    }

    @Override
    public boolean wasCarriedOut() {
        return true;
    }

    protected final String getAppendedTextNames(Collection<LotroPhysicalCard> cards) {
        return GameUtils.getAppendedTextNames(cards);
    }

    protected final String getAppendedNames(Collection<? extends LotroPhysicalCard> cards) {
        return GameUtils.getAppendedNames(cards);
    }
}
