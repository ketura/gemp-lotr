package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromHandResult;

import java.util.Collection;

public class DiscardCardsFromHandEffect extends AbstractEffect {
    private PhysicalCard _source;
    private Collection<PhysicalCard> _cards;

    public DiscardCardsFromHandEffect(PhysicalCard source, Collection<PhysicalCard> cards) {
        _source = source;
        _cards = cards;
    }

    @Override
    public String getText(LotroGame game) {
        return "Discard from hand - " + getAppendedTextNames(_cards);
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.DISCARD_FROM_HAND;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        for (PhysicalCard card : _cards) {
            if (card.getZone() != Zone.HAND)
                return false;
        }
        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        GameState gameState = game.getGameState();
        gameState.sendMessage(getAppendedNames(_cards) + " is/are discarded from hand");
        for (PhysicalCard card : _cards) {
            gameState.removeCardFromZone(card);
            gameState.addCardToZone(card, Zone.DISCARD);
        }

        return new FullEffectResult(new EffectResult[]{new DiscardCardsFromHandResult(_source, _cards)}, true, true);
    }
}
