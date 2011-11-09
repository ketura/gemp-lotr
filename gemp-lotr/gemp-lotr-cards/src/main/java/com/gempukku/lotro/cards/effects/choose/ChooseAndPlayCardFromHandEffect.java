package com.gempukku.lotro.cards.effects.choose;

import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ChooseAndPlayCardFromHandEffect implements Effect {
    private String _playerId;
    private Filter _filter;
    private int _twilightModifier;
    private CostToEffectAction _playCardAction;

    public ChooseAndPlayCardFromHandEffect(String playerId, List<? extends PhysicalCard> cardsInHandAtStart, Filterable... filter) {
        this(playerId, cardsInHandAtStart, 0, filter);
    }

    public ChooseAndPlayCardFromHandEffect(String playerId, List<? extends PhysicalCard> cardsInHandAtStart, int twilightModifier, Filterable... filter) {
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
    public Effect.Type getType() {
        return null;
    }

    @Override
    public Collection<? extends EffectResult> playEffect(final LotroGame game) {
        Collection<PhysicalCard> playableInHand = getPlayableInHandCards(game);
        if (playableInHand.size() > 0) {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new CardsSelectionDecision(1, "Choose a card to play", playableInHand, 1, 1) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            final PhysicalCard selectedCard = getSelectedCardsByResponse(result).iterator().next();
                            _playCardAction = selectedCard.getBlueprint().getPlayCardAction(_playerId, game, selectedCard, _twilightModifier);
                            _playCardAction.appendEffect(
                                    new UnrespondableEffect() {
                                        @Override
                                        protected void doPlayEffect(LotroGame game) {
                                            afterCardPlayed(selectedCard);
                                        }
                                    });
                            game.getActionsEnvironment().addActionToStack(_playCardAction);
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
