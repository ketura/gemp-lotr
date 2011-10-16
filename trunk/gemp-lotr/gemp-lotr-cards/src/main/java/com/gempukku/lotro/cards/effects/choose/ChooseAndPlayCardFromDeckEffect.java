package com.gempukku.lotro.cards.effects.choose;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ChooseAndPlayCardFromDeckEffect extends AbstractEffect {
    private String _playerId;
    private Filterable[] _filter;
    private int _twilightModifier;

    public ChooseAndPlayCardFromDeckEffect(String playerId, Filterable... filter) {
        this(playerId, 0, filter);
    }

    public ChooseAndPlayCardFromDeckEffect(String playerId, int twilightModifier, Filterable... filter) {
        _playerId = playerId;
        _filter = filter;
        _twilightModifier = twilightModifier;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }

    @Override
    public String getText(LotroGame game) {
        return "Play card from deck";
    }

    @Override
    protected FullEffectResult playEffectReturningResult(final LotroGame game) {
        Collection<PhysicalCard> deck = Filters.filter(game.getGameState().getDeck(_playerId), game.getGameState(), game.getModifiersQuerying(), Filters.and(_filter, Filters.playable(game, _twilightModifier)));
        game.getUserFeedback().sendAwaitingDecision(_playerId,
                new ArbitraryCardsSelectionDecision(1, "Choose a card to play", new LinkedList<PhysicalCard>(deck), 0, 1) {
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
}
