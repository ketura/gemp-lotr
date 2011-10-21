package com.gempukku.lotro.cards.effects.choose;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ChooseAndPlayCardFromDiscardEffect extends AbstractEffect {
    private String _playerId;
    private Filter _filter;
    private int _twilightModifier;

    public ChooseAndPlayCardFromDiscardEffect(String playerId, List<? extends PhysicalCard> cardsInDiscardAtStart, Filterable... filter) {
        this(playerId, cardsInDiscardAtStart, 0, filter);
    }

    public ChooseAndPlayCardFromDiscardEffect(String playerId, List<? extends PhysicalCard> cardsInDiscardAtStart, int twilightModifier, Filterable... filter) {
        _playerId = playerId;
        // Card has to be in discard when you start playing the card (we need to copy the collection)
        _filter = Filters.and(filter, Filters.in(new LinkedList<PhysicalCard>(cardsInDiscardAtStart)));
        _twilightModifier = twilightModifier;
    }

    @Override
    public String getText(LotroGame game) {
        return "Play card from discard";
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return getPlayableInDiscard(game).size() > 0;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(final LotroGame game) {
        Collection<PhysicalCard> discard = getPlayableInDiscard(game);
        if (discard.size() > 0) {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new ArbitraryCardsSelectionDecision(1, "Choose a card to play", new LinkedList<PhysicalCard>(discard), 1, 1) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            List<PhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                            if (selectedCards.size() > 0) {
                                PhysicalCard selectedCard = selectedCards.get(0);
                                game.getActionsEnvironment().addActionToStack(selectedCard.getBlueprint().getPlayCardAction(_playerId, game, selectedCard, _twilightModifier));
                            }
                        }
                    });
            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }

    private Collection<PhysicalCard> getPlayableInDiscard(LotroGame game) {
        return Filters.filter(game.getGameState().getDiscard(_playerId), game.getGameState(), game.getModifiersQuerying(), _filter, Filters.playable(game, _twilightModifier));
    }
}
