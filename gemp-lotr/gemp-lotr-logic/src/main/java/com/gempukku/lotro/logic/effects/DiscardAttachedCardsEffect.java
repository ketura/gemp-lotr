package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.List;

public class DiscardAttachedCardsEffect extends UnrespondableEffect {
    private PhysicalCard _card;

    public DiscardAttachedCardsEffect(PhysicalCard card) {
        _card = card;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        GameState gameState = game.getGameState();
        List<PhysicalCard> attachedCards = gameState.getAttachedCards(_card);
        for (PhysicalCard attachedCard : attachedCards) {
            gameState.stopAffecting(attachedCard);
            gameState.removeCardFromZone(attachedCard);
            gameState.addCardToZone(attachedCard, Zone.DISCARD);
        }
    }
}
