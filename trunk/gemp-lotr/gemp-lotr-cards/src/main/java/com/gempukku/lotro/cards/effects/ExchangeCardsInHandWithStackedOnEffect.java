package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExchangeCardsInHandWithStackedOnEffect extends AbstractEffect {
    private String _performingPlayer;
    private PhysicalCard _source;
    private String _playerHand;
    private PhysicalCard _stackedOn;

    public ExchangeCardsInHandWithStackedOnEffect(String performingPlayer, PhysicalCard source, String playerHand, PhysicalCard stackedOn) {
        _performingPlayer = performingPlayer;
        _source = source;
        _playerHand = playerHand;
        _stackedOn = stackedOn;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            final List<PhysicalCard> stackedCards = game.getGameState().getStackedCards(_stackedOn);
            final List<? extends PhysicalCard> hand = game.getGameState().getHand(_playerHand);

            Set<PhysicalCard> toRemove = new HashSet<PhysicalCard>();
            toRemove.addAll(stackedCards);
            toRemove.addAll(hand);

            game.getGameState().removeCardsFromZone(_performingPlayer, toRemove);
            for (PhysicalCard cardInHand : hand)
                game.getGameState().stackCard(game, cardInHand, _stackedOn);
            for (PhysicalCard stackedCard : stackedCards)
                game.getGameState().addCardToZone(game, stackedCard, Zone.HAND);

            return new FullEffectResult(true, true);
        }
        return new FullEffectResult(false, false);
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _stackedOn.getZone().isInPlay();
    }
}
