package com.gempukku.lotro.cards.effects.choose;

import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ChooseAndPlayCardFromDeadPileEffect implements Effect {
    private String _playerId;
    private Filter _filter;
    private int _twilightModifier;
    private Action _playCardAction;

    public ChooseAndPlayCardFromDeadPileEffect(String playerId, List<? extends PhysicalCard> cardsInDeadPileAtStart, Filterable... filter) {
        this(playerId, cardsInDeadPileAtStart, 0, filter);
    }

    public ChooseAndPlayCardFromDeadPileEffect(String playerId, List<? extends PhysicalCard> cardsInDeadPileAtStart, int twilightModifier, Filterable... filter) {
        _playerId = playerId;
        // Card has to be in dead pile when you start playing the card (we need to copy the collection)
        _filter = Filters.and(filter, Filters.in(new LinkedList<PhysicalCard>(cardsInDeadPileAtStart)));
        _twilightModifier = twilightModifier;
    }

    @Override
    public String getText(LotroGame game) {
        return "Play card from dead pile";
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return getPlayableInDeadPile(game).size() > 0;
    }

    @Override
    public Type getType() {
        return null;
    }

    private Collection<PhysicalCard> getPlayableInDeadPile(LotroGame game) {
        return Filters.filter(game.getGameState().getDiscard(_playerId), game.getGameState(), game.getModifiersQuerying(), _filter, Filters.playable(game, _twilightModifier));
    }

    @Override
    public Collection<? extends EffectResult> playEffect(final LotroGame game) {
        Collection<PhysicalCard> deadPile = getPlayableInDeadPile(game);
        if (deadPile.size() > 0) {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new ArbitraryCardsSelectionDecision(1, "Choose a card to play", new LinkedList<PhysicalCard>(deadPile), 1, 1) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            List<PhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                            if (selectedCards.size() > 0) {
                                PhysicalCard selectedCard = selectedCards.get(0);
                                _playCardAction = selectedCard.getBlueprint().getPlayCardAction(_playerId, game, selectedCard, _twilightModifier);
                                game.getActionsEnvironment().addActionToStack(_playCardAction);
                            }
                        }
                    });
        }
        return null;
    }

    @Override
    public boolean wasSuccessful() {
        if (_playCardAction == null)
            return false;
        if (_playCardAction instanceof PlayPermanentAction)
            return ((PlayPermanentAction) _playCardAction).wasSuccessful();
        return true;
    }

    @Override
    public boolean wasCarriedOut() {
        if (_playCardAction == null)
            return false;
        if (_playCardAction instanceof PlayPermanentAction)
            return ((PlayPermanentAction) _playCardAction).wasCarriedOut();
        return true;
    }
}