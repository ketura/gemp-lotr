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
    public boolean wasSuccessful() {
        return true;
    }

    @Override
    public boolean wasCarriedOut() {
        return true;
    }

    protected final String getAppendedTextNames(Collection<PhysicalCard> cards) {
        StringBuilder sb = new StringBuilder();
        for (PhysicalCard card : cards)
            sb.append(card.getBlueprint().getName() + ", ");

        if (sb.length() == 0)
            return "none";
        else
            return sb.substring(0, sb.length() - 2);
    }

    protected final String getAppendedNames(Collection<PhysicalCard> cards) {
        StringBuilder sb = new StringBuilder();
        for (PhysicalCard card : cards)
            sb.append(GameUtils.getCardLink(card) + ", ");

        if (sb.length() == 0)
            return "none";
        else
            return sb.substring(0, sb.length() - 2);
    }

    @Override
    public void reset() {

    }
}
