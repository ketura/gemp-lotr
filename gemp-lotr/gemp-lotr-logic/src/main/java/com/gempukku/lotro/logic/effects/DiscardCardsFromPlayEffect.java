package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.List;

public class DiscardCardsFromPlayEffect extends UnrespondableEffect {
    private PhysicalCard _source;
    private Filter _filter;

    public DiscardCardsFromPlayEffect(PhysicalCard source, Filter filter) {
        _source = source;
        _filter = filter;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), _filter);
    }

    @Override
    public void playEffect(LotroGame game) {
        List<PhysicalCard> cardsToDiscard = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), _filter);
        for (PhysicalCard card : cardsToDiscard) {
            if (game.getModifiersQuerying().canBeDiscardedFromPlay(game.getGameState(), card, _source)) {
                GameState gameState = game.getGameState();
                gameState.stopAffecting(card);
                gameState.removeCardFromZone(card);
                gameState.addCardToZone(card, Zone.DISCARD);

                List<PhysicalCard> attachedCards = gameState.getAttachedCards(card);
                for (PhysicalCard attachedCard : attachedCards) {
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
    }
}
