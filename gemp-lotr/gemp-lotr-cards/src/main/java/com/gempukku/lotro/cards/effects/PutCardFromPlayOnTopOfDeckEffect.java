package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PutCardFromPlayOnTopOfDeckEffect extends AbstractEffect {
    private PhysicalCard _physicalCard;

    public PutCardFromPlayOnTopOfDeckEffect(PhysicalCard physicalCard) {
        _physicalCard = physicalCard;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _physicalCard.getZone().isInPlay();
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            final List<PhysicalCard> attachedCards = game.getGameState().getAttachedCards(_physicalCard);
            final List<PhysicalCard> stackedCards = game.getGameState().getStackedCards(_physicalCard);

            Set<PhysicalCard> removedFromZone = new HashSet<PhysicalCard>();

            removedFromZone.add(_physicalCard);
            removedFromZone.addAll(attachedCards);
            removedFromZone.addAll(stackedCards);

            GameState gameState = game.getGameState();

            gameState.removeCardsFromZone(_physicalCard.getOwner(), removedFromZone);

            gameState.putCardOnTopOfDeck(_physicalCard);
            for (PhysicalCard attachedCard : attachedCards) {
                gameState.addCardToZone(game, attachedCard, Zone.DISCARD);
                game.getActionsEnvironment().emitEffectResult(
                        new DiscardCardsFromPlayResult(attachedCard));
            }
            for (PhysicalCard stackedCard : stackedCards)
                gameState.addCardToZone(game, stackedCard, Zone.DISCARD);

            gameState.sendMessage(_physicalCard.getOwner() + " puts " + GameUtils.getCardLink(_physicalCard) + " from play on the top of deck");

            return new FullEffectResult(true, true);
        }
        return new FullEffectResult(false, false);
    }

    @Override
    public String getText(LotroGame game) {
        return "Put " + GameUtils.getCardLink(_physicalCard) + " from play on top of deck";
    }

    @Override
    public Type getType() {
        return null;
    }
}
