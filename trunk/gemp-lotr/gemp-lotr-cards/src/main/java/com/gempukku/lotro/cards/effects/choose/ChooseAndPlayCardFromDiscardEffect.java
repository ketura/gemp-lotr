package com.gempukku.lotro.cards.effects.choose;

import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ChooseAndPlayCardFromDiscardEffect implements Effect {
    private String _playerId;
    private Filter _filter;
    private int _twilightModifier;
    private CostToEffectAction _playCardAction;

    public ChooseAndPlayCardFromDiscardEffect(String playerId, LotroGame game, Filterable... filter) {
        this(playerId, game, 0, filter);
    }

    public ChooseAndPlayCardFromDiscardEffect(String playerId, LotroGame game, int twilightModifier, Filterable... filter) {
        _playerId = playerId;
        // Card has to be in discard when you start playing the card (we need to copy the collection)
        _filter = Filters.and(filter, Filters.in(new LinkedList<PhysicalCard>(game.getGameState().getDiscard(playerId))));
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

    private Collection<PhysicalCard> getPlayableInDiscard(LotroGame game) {
        return Filters.filter(game.getGameState().getDiscard(_playerId), game.getGameState(), game.getModifiersQuerying(), _filter, Filters.playable(game, _twilightModifier));
    }

    @Override
    public Collection<? extends EffectResult> playEffect(final LotroGame game) {
        Collection<PhysicalCard> discard = getPlayableInDiscard(game);
        if (discard.size() > 0) {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new ArbitraryCardsSelectionDecision(1, "Choose a card to play", new LinkedList<PhysicalCard>(discard), 1, 1) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            List<PhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                            if (selectedCards.size() > 0) {
                                final PhysicalCard selectedCard = selectedCards.get(0);
                                _playCardAction = selectedCard.getBlueprint().getPlayCardAction(_playerId, game, selectedCard, _twilightModifier, false);
                                _playCardAction.appendEffect(
                                        new UnrespondableEffect() {
                                            @Override
                                            protected void doPlayEffect(LotroGame game) {
                                                afterCardPlayed(selectedCard);
                                            }
                                        });
                                game.getActionsEnvironment().addActionToStack(_playCardAction);
                            }
                        }
                    });
        }
        return null;
    }

    protected void afterCardPlayed(PhysicalCard cardPlayed) {
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
