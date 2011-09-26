package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DiscardCardsFromPlayEffect extends AbstractPreventableCardEffect {
    private PhysicalCard _source;

    public DiscardCardsFromPlayEffect(PhysicalCard source, PhysicalCard... cards) {
        super(cards);
        _source = source;
    }

    public DiscardCardsFromPlayEffect(PhysicalCard source, Filter filter) {
        super(filter);
        _source = source;
    }

    @Override
    protected Filter getExtraAffectableFilter() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return (_source == null || modifiersQuerying.canBeDiscardedFromPlay(gameState, physicalCard, _source));
            }
        };
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.DISCARD_FROM_PLAY;
    }


    @Override
    public String getText(LotroGame game) {
        Collection<PhysicalCard> cards = getCardsToBeAffected(game);
        return "Discard - " + getAppendedNames(cards);
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        Collection<PhysicalCard> cardsToDiscard = getCardsToBeAffected(game);

        Set<PhysicalCard> discardedCards = new HashSet<PhysicalCard>();
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
            game.getGameState().sendMessage(_source.getOwner() + " discards " + getAppendedNames(discardedCards) + " from play using " + _source.getBlueprint().getName());
        return new EffectResult[]{new DiscardCardsFromPlayResult(discardedCards)};
    }
}
