package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.List;

public class ChooseAndPlayCardFromDiscardEffect extends UnrespondableEffect {
    private String _playerId;
    private Filter _filter;
    private int _twilightModifier;

    public ChooseAndPlayCardFromDiscardEffect(String playerId, Filter filter) {
        this(playerId, filter, 0);
    }

    public ChooseAndPlayCardFromDiscardEffect(String playerId, Filter filter, int twilightModifier) {
        _playerId = playerId;
        _filter = filter;
        _twilightModifier = twilightModifier;
    }

    @Override
    public String getText() {
        return "Play card from discard";
    }

    @Override
    public void doPlayEffect(final LotroGame game) {
        List<PhysicalCard> discard = Filters.filter(game.getGameState().getDiscard(_playerId), game.getGameState(), game.getModifiersQuerying(), _filter, Filters.playable(game, _twilightModifier));
        game.getUserFeedback().sendAwaitingDecision(_playerId,
                new ArbitraryCardsSelectionDecision(1, "Choose a card to play", discard, 0, 1) {
                    @Override
                    public void decisionMade(String result) throws DecisionResultInvalidException {
                        List<PhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                        if (selectedCards.size() > 0) {
                            PhysicalCard selectedCard = selectedCards.get(0);
                            game.getActionsEnvironment().addActionToStack(selectedCard.getBlueprint().getPlayCardAction(_playerId, game, selectedCard, _twilightModifier));
                        }
                    }
                });
    }
}
