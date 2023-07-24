package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.game.timing.PlayOrder;
import com.gempukku.lotro.game.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.game.timing.results.RevealCardFromHandResult;

import java.util.Collection;
import java.util.Collections;

public class RevealCardsFromYourHandEffect extends AbstractEffect {
    private final LotroPhysicalCard _source;
    private final String _handPlayerId;
    private final Collection<? extends LotroPhysicalCard> _cards;

    public RevealCardsFromYourHandEffect(LotroPhysicalCard source, String handPlayerId, LotroPhysicalCard card) {
        this(source, handPlayerId, Collections.singleton(card));
    }

    public RevealCardsFromYourHandEffect(LotroPhysicalCard source, String handPlayerId, Collection<? extends LotroPhysicalCard> cards) {
        _source = source;
        _handPlayerId = handPlayerId;
        _cards = cards;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Reveal cards from hand";
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        for (LotroPhysicalCard card : _cards) {
            if (card.getZone() != Zone.HAND)
                return false;
        }

        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " revealed " + _handPlayerId + " cards in hand - " + getAppendedNames(_cards));

        final PlayOrder playerOrder = game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(_handPlayerId, false);
        // Skip hand owner
        playerOrder.getNextPlayer();

        String nextPlayer;
        while ((nextPlayer = playerOrder.getNextPlayer()) != null) {
            game.getUserFeedback().sendAwaitingDecision(nextPlayer,
                    new ArbitraryCardsSelectionDecision(1, _handPlayerId + " revealed card(s) in hand", _cards, Collections.emptySet(), 0, 0) {
                        @Override
                        public void decisionMade(String result) {
                        }
                    });
        }

        for (LotroPhysicalCard card : _cards) {
            game.getActionsEnvironment().emitEffectResult(new RevealCardFromHandResult(_source, _handPlayerId, card));
        }

        return new FullEffectResult(true);
    }
}
