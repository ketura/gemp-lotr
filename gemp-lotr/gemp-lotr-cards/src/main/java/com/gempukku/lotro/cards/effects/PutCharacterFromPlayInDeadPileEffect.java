package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PutCharacterFromPlayInDeadPileEffect extends AbstractEffect {
    private PhysicalCard _card;

    public PutCharacterFromPlayInDeadPileEffect(PhysicalCard card) {
        _card = card;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _card.getZone() != null && _card.getZone().isInPlay();
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            GameState gameState = game.getGameState();

            gameState.sendMessage(GameUtils.getCardLink(_card) + " is put into dead pile");

            // For result
            Set<PhysicalCard> discardedCards = new HashSet<PhysicalCard>();

            // Prepare the moves
            Set<PhysicalCard> toRemoveFromZone = new HashSet<PhysicalCard>();
            Set<PhysicalCard> toAddToDeadPile = new HashSet<PhysicalCard>();
            Set<PhysicalCard> toAddToDiscard = new HashSet<PhysicalCard>();

            toRemoveFromZone.add(_card);

            toAddToDeadPile.add(_card);

            List<PhysicalCard> attachedCards = gameState.getAttachedCards(_card);
            discardedCards.addAll(attachedCards);
            toRemoveFromZone.addAll(attachedCards);
            toAddToDiscard.addAll(attachedCards);

            List<PhysicalCard> stackedCards = gameState.getStackedCards(_card);
            toRemoveFromZone.addAll(stackedCards);
            toAddToDiscard.addAll(stackedCards);

            gameState.removeCardsFromZone(null, toRemoveFromZone);

            for (PhysicalCard deadCard : toAddToDeadPile)
                gameState.addCardToZone(game, deadCard, Zone.DEAD);

            for (PhysicalCard discardedCard : toAddToDiscard)
                gameState.addCardToZone(game, discardedCard, Zone.DISCARD);

            if (discardedCards.size() > 0) {
                return new FullEffectResult(Arrays.asList(new DiscardCardsFromPlayResult(discardedCards)), true, true);
            } else {
                return new FullEffectResult(null, true, true);
            }
        }
        return new FullEffectResult(null, false, false);
    }
}
