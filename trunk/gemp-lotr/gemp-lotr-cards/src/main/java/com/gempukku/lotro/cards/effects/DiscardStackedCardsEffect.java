package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;

public class DiscardStackedCardsEffect extends AbstractEffect {
    private PhysicalCard _source;
    private Collection<PhysicalCard> _cards;

    public DiscardStackedCardsEffect(PhysicalCard source, Collection<PhysicalCard> cards) {
        _source = source;
        _cards = cards;
    }

    @Override
    public String getText(LotroGame game) {
        return "Discard stacked - " + getAppendedTextNames(_cards);
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        for (PhysicalCard card : _cards) {
            if (card.getZone() != Zone.STACKED)
                return false;
        }
        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        GameState gameState = game.getGameState();
        gameState.sendMessage(getAppendedNames(_cards) + " is/are discarded from being stacked");
        gameState.removeCardsFromZone(_cards);
        for (PhysicalCard card : _cards) {
            gameState.addCardToZone(card, Zone.DISCARD);
        }

        return new FullEffectResult(null, true, true);
    }
}
