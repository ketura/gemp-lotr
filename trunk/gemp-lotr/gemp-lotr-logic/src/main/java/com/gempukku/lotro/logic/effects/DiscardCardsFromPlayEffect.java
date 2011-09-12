package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.LinkedList;
import java.util.List;

public class DiscardCardsFromPlayEffect extends AbstractEffect {
    private PhysicalCard _source;
    private Filter _filter;
    private List<PhysicalCard> _discardedCards;

    public DiscardCardsFromPlayEffect(PhysicalCard source, Filter filter) {
        _source = source;
        _filter = filter;
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.DISCARD_FROM_PLAY;
    }

    @Override
    public String getText() {
        return "Discard multiple cards from play";
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), _filter);
    }

    @Override
    public EffectResult playEffect(LotroGame game) {
        List<PhysicalCard> cardsToDiscard = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), _filter);
        _discardedCards = new LinkedList<PhysicalCard>();
        for (PhysicalCard card : cardsToDiscard) {
            if (game.getModifiersQuerying().canBeDiscardedFromPlay(game.getGameState(), card, _source)) {
                _discardedCards.add(card);

                GameState gameState = game.getGameState();
                gameState.stopAffecting(card);
                gameState.removeCardFromZone(card);
                gameState.addCardToZone(card, Zone.DISCARD);

                List<PhysicalCard> attachedCards = gameState.getAttachedCards(card);
                for (PhysicalCard attachedCard : attachedCards) {
                    _discardedCards.add(attachedCard);

                    gameState.stopAffecting(attachedCard);
                    gameState.removeCardFromZone(attachedCard);
                    gameState.addCardToZone(attachedCard, Zone.DISCARD);
                }

                List<PhysicalCard> stackedCards = gameState.getStackedCards(card);
                for (PhysicalCard stackedCard : stackedCards) {
                    gameState.removeCardFromZone(stackedCard);
                    gameState.addCardToZone(stackedCard, Zone.DISCARD);
                }
            }
        }
        return new DiscardCardsFromPlayResult(_discardedCards);
    }
}
