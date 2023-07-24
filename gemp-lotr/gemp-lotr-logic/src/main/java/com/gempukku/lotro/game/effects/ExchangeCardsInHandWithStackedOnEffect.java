package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.DefaultGame;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ExchangeCardsInHandWithStackedOnEffect extends AbstractEffect {
    private final String _performingPlayer;
    private final LotroPhysicalCard _source;
    private final String _playerHand;
    private final LotroPhysicalCard _stackedOn;

    public ExchangeCardsInHandWithStackedOnEffect(String performingPlayer, LotroPhysicalCard source, String playerHand, LotroPhysicalCard stackedOn) {
        _performingPlayer = performingPlayer;
        _source = source;
        _playerHand = playerHand;
        _stackedOn = stackedOn;
    }

    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (isPlayableInFull(game)) {
            final List<LotroPhysicalCard> stackedCards = new LinkedList<>(game.getGameState().getStackedCards(_stackedOn));
            final List<LotroPhysicalCard> hand = new LinkedList<>(game.getGameState().getHand(_playerHand));

            Set<LotroPhysicalCard> toRemove = new HashSet<>();
            toRemove.addAll(stackedCards);
            toRemove.addAll(hand);

            game.getGameState().removeCardsFromZone(_performingPlayer, toRemove);
            
            for (LotroPhysicalCard cardInHand : hand)
                game.getGameState().stackCard(game, cardInHand, _stackedOn);

            for (LotroPhysicalCard stackedCard : stackedCards)
                game.getGameState().addCardToZone(game, stackedCard, Zone.HAND);

            return new FullEffectResult(true);
        }
        return new FullEffectResult(false);
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return _stackedOn.getZone().isInPlay();
    }
}
