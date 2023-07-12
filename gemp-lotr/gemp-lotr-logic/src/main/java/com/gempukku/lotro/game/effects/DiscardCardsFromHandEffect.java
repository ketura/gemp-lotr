package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.game.timing.results.DiscardCardFromHandResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DiscardCardsFromHandEffect extends AbstractEffect {
    private final PhysicalCard _source;
    private final String _playerId;
    private final Collection<? extends PhysicalCard> _cards;
    private final boolean _forced;

    public DiscardCardsFromHandEffect(PhysicalCard source, String playerId, Collection<? extends PhysicalCard> cards, boolean forced) {
        _source = source;
        _playerId = playerId;
        _cards = cards;
        _forced = forced;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Discard from hand - " + getAppendedTextNames(_cards);
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        for (PhysicalCard card : _cards) {
            if (card.getZone() != Zone.HAND)
                return false;
        }
        if (_forced && !game.getModifiersQuerying().canDiscardCardsFromHand(game, _playerId, _source))
            return false;
        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (!_forced || game.getModifiersQuerying().canDiscardCardsFromHand(game, _playerId, _source)) {
            GameState gameState = game.getGameState();

            Set<PhysicalCard> discardedCards = new HashSet<>();
            for (PhysicalCard card : _cards)
                if (card.getZone() == Zone.HAND)
                    discardedCards.add(card);

            if (discardedCards.size() > 0)
                gameState.sendMessage(getAppendedNames(discardedCards) + " " + GameUtils.be(discardedCards) + " discarded from hand");
            String sourcePlayer = null;
            if (_source != null)
                sourcePlayer = _source.getOwner();
            gameState.removeCardsFromZone(sourcePlayer, discardedCards);
            for (PhysicalCard card : discardedCards) {
                gameState.addCardToZone(game, card, Zone.DISCARD);
                game.getActionsEnvironment().emitEffectResult(new DiscardCardFromHandResult(_source, card, _playerId, _forced));
            }

            return new FullEffectResult(discardedCards.size() == _cards.size());
        }

        return new FullEffectResult(false);
    }
}
