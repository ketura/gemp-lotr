package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StackCardFromPlayEffect extends AbstractEffect {
    private PhysicalCard _card;
    private PhysicalCard _stackOn;

    public StackCardFromPlayEffect(PhysicalCard card, PhysicalCard stackOn) {
        _card = card;
        _stackOn = stackOn;
    }

    @Override
    public String getText(LotroGame game) {
        return "Stack " + GameUtils.getCardLink(_card) + " on " + GameUtils.getCardLink(_stackOn);
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _card.getZone().isInPlay() && _stackOn.getZone().isInPlay();
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            GameState gameState = game.getGameState();

            List<PhysicalCard> attachedCards = gameState.getAttachedCards(_card);
            List<PhysicalCard> stackedCards = gameState.getStackedCards(_card);

            // Then remove from zones (card, attached and stacked on it)
            Set<PhysicalCard> discardedCards = new HashSet<PhysicalCard>();
            Set<PhysicalCard> cardsToRemove = new HashSet<PhysicalCard>();
            cardsToRemove.add(_card);
            cardsToRemove.addAll(attachedCards);
            discardedCards.addAll(attachedCards);
            cardsToRemove.addAll(stackedCards);

            gameState.removeCardsFromZone(cardsToRemove);

            // And put them in new zones (attached and stacked to discard, the card gets stacked on)
            for (PhysicalCard attachedCard : attachedCards)
                gameState.addCardToZone(game, attachedCard, Zone.DISCARD);

            for (PhysicalCard stackedCard : stackedCards)
                gameState.addCardToZone(game, stackedCard, Zone.DISCARD);

            game.getGameState().sendMessage(GameUtils.getCardLink(_card) + " is stacked on " + GameUtils.getCardLink(_stackOn));
            game.getGameState().stackCard(game, _card, _stackOn);

            // Send the result (attached cards get discarded)
            if (discardedCards.size() > 0)
                return new FullEffectResult(Collections.singleton(new DiscardCardsFromPlayResult(discardedCards)), true, true);
            else
                return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}
