package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DiscardAttachedCardsEffect implements Effect {
    private PhysicalCard _card;

    public DiscardAttachedCardsEffect(PhysicalCard card) {
        _card = card;
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.DISCARD_FROM_PLAY;
    }

    @Override
    public String getText(LotroGame game) {
        return "Discard attached cards";
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        Set<PhysicalCard> discardedCards = new HashSet<PhysicalCard>();
        GameState gameState = game.getGameState();
        List<PhysicalCard> attachedCards = gameState.getAttachedCards(_card);
        for (PhysicalCard attachedCard : attachedCards) {
            gameState.stopAffecting(attachedCard);
            gameState.removeCardFromZone(attachedCard);
            gameState.addCardToZone(attachedCard, Zone.DISCARD);
            discardedCards.add(attachedCard);
        }

        return new EffectResult[]{new DiscardCardsFromPlayResult(discardedCards)};
    }
}
