package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.LinkedList;
import java.util.List;

public class DiscardAttachedCardsEffect extends AbstractEffect {
    private PhysicalCard _card;

    public DiscardAttachedCardsEffect(PhysicalCard card) {
        _card = card;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return true;
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.DISCARD_FROM_PLAY;
    }

    @Override
    public String getText() {
        return "Discard attached cards";
    }

    @Override
    public EffectResult playEffect(LotroGame game) {
        List<PhysicalCard> discardedCards = new LinkedList<PhysicalCard>();
        GameState gameState = game.getGameState();
        List<PhysicalCard> attachedCards = gameState.getAttachedCards(_card);
        for (PhysicalCard attachedCard : attachedCards) {
            gameState.stopAffecting(attachedCard);
            gameState.removeCardFromZone(attachedCard);
            gameState.addCardToZone(attachedCard, Zone.DISCARD);
            discardedCards.add(attachedCard);
        }

        return new DiscardCardsFromPlayResult(discardedCards);
    }
}
