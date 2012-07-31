package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.effects.DiscardUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PutCardFromPlayOnBottomOfDeckEffect extends AbstractEffect {
    private PhysicalCard _physicalCard;

    public PutCardFromPlayOnBottomOfDeckEffect(PhysicalCard physicalCard) {
        _physicalCard = physicalCard;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _physicalCard.getZone().isInPlay();
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            Set<PhysicalCard> discardedCards = new HashSet<PhysicalCard>();
            Set<PhysicalCard> toGoToDiscardCards = new HashSet<PhysicalCard>();

            DiscardUtils.cardsToChangeZones(game.getGameState(), Collections.singleton(_physicalCard), discardedCards, toGoToDiscardCards);

            GameState gameState = game.getGameState();

            Set<PhysicalCard> removeFromPlay = new HashSet<PhysicalCard>(toGoToDiscardCards);
            removeFromPlay.add(_physicalCard);

            gameState.removeCardsFromZone(_physicalCard.getOwner(), removeFromPlay);

            gameState.putCardOnBottomOfDeck(_physicalCard);
            for (PhysicalCard discardedCard : discardedCards) {
                game.getActionsEnvironment().emitEffectResult(
                        new DiscardCardsFromPlayResult(discardedCard));
            }
            for (PhysicalCard toGoToDiscardCard : toGoToDiscardCards)
                gameState.addCardToZone(game, toGoToDiscardCard, Zone.DISCARD);

            gameState.sendMessage(_physicalCard.getOwner() + " puts " + GameUtils.getCardLink(_physicalCard) + " from play on the bottom of deck");

            return new FullEffectResult(true, true);
        }
        return new FullEffectResult(false, false);
    }

    @Override
    public String getText(LotroGame game) {
        return "Put " + GameUtils.getCardLink(_physicalCard) + " from play on bottom of deck";
    }

    @Override
    public Type getType() {
        return null;
    }
}