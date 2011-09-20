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

    public DiscardCardsFromPlayEffect(PhysicalCard source, Filter filter) {
        _source = source;
        _filter = filter;
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.DISCARD_FROM_PLAY;
    }

    public List<PhysicalCard> getCardsToBeDiscarded(LotroGame game) {
        List<PhysicalCard> cardsToDiscard = new LinkedList<PhysicalCard>();
        for (PhysicalCard physicalCard : Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), _filter)) {
            if (_source == null || game.getModifiersQuerying().canBeDiscardedFromPlay(game.getGameState(), physicalCard, _source))
                cardsToDiscard.add(physicalCard);
        }
        return cardsToDiscard;
    }

    @Override
    public String getText(LotroGame game) {
        List<PhysicalCard> cards = getCardsToBeDiscarded(game);
        return "Discard - " + getAppendedNames(cards);
    }

    private String getAppendedNames(List<PhysicalCard> cards) {
        StringBuilder sb = new StringBuilder();
        for (PhysicalCard card : cards)
            sb.append(card.getBlueprint().getName() + ", ");

        if (sb.length() == 0)
            return "none";
        else
            return sb.substring(0, sb.length() - 1);
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), _filter);
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        List<PhysicalCard> cardsToDiscard = getCardsToBeDiscarded(game);
        List<PhysicalCard> discardedCards = new LinkedList<PhysicalCard>();
        for (PhysicalCard card : cardsToDiscard) {
            discardedCards.add(card);

            GameState gameState = game.getGameState();
            gameState.stopAffecting(card);
            gameState.removeCardFromZone(card);
            gameState.addCardToZone(card, Zone.DISCARD);

            List<PhysicalCard> attachedCards = gameState.getAttachedCards(card);
            for (PhysicalCard attachedCard : attachedCards) {
                discardedCards.add(attachedCard);

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

        if (_source != null && discardedCards.size() > 0)
            game.getGameState().sendMessage(_source.getOwner() + " discards multiple cards from play using " + _source.getBlueprint().getName());
        return new EffectResult[]{new DiscardCardsFromPlayResult(discardedCards)};
    }
}
