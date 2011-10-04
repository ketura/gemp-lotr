package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ChooseAndPlayCardFromHandEffect extends AbstractEffect {
    private String _playerId;
    private Filter _filter;
    private int _twilightModifier;

    public ChooseAndPlayCardFromHandEffect(String playerId, List<? extends PhysicalCard> cardsInHandAtStart, Filter filter) {
        this(playerId, cardsInHandAtStart, filter, 0);
    }

    public ChooseAndPlayCardFromHandEffect(String playerId, List<? extends PhysicalCard> cardsInHandAtStart, Filter filter, int twilightModifier) {
        _playerId = playerId;
        // Card has to be in hand when you start playing the card (we need to copy the collection)
        _filter = Filters.and(filter, Filters.in(new LinkedList<PhysicalCard>(cardsInHandAtStart)));
        _twilightModifier = twilightModifier;
    }

    @Override
    public String getText(LotroGame game) {
        return "Play card from hand";
    }

    private Collection<PhysicalCard> getPlayableInHandCards(LotroGame game) {
        return Filters.filter(game.getGameState().getHand(_playerId), game.getGameState(), game.getModifiersQuerying(), _filter, Filters.playable(game, _twilightModifier));
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return getPlayableInHandCards(game).size() > 0;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(final LotroGame game) {
        Collection<PhysicalCard> playableInHand = getPlayableInHandCards(game);
        if (playableInHand.size() > 0) {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new CardsSelectionDecision(1, "Choose a card to play", playableInHand, 1, 1) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            final PhysicalCard selectedCard = getSelectedCardsByResponse(result).iterator().next();
                            game.getActionsEnvironment().addActionToStack(selectedCard.getBlueprint().getPlayCardAction(_playerId, game, selectedCard, _twilightModifier));
                        }
                    });
            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}
