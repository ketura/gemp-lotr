package com.gempukku.lotro.cards.effects.choose;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.List;

public abstract class ChooseArbitraryCardsEffect extends AbstractEffect {
    private String _playerId;
    private String _choiceText;
    private List<? extends PhysicalCard> _cards;
    private Filter _filter;
    private int _minimum;
    private int _maximum;

    public ChooseArbitraryCardsEffect(String playerId, String choiceText, List<? extends PhysicalCard> cards, int minimum, int maximum) {
        this(playerId, choiceText, cards, Filters.any, minimum, maximum);
    }

    public ChooseArbitraryCardsEffect(String playerId, String choiceText, List<? extends PhysicalCard> cards, Filter filter, int minimum, int maximum) {
        _playerId = playerId;
        _choiceText = choiceText;
        _cards = cards;
        _filter = filter;
        _minimum = minimum;
        _maximum = maximum;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return Filters.filter(_cards, game.getGameState(), game.getModifiersQuerying(), _filter).size() >= _minimum;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(final LotroGame game) {
        Collection<PhysicalCard> possibleCards = Filters.filter(_cards, game.getGameState(), game.getModifiersQuerying(), _filter);

        boolean success = possibleCards.size() >= _minimum;

        int minimum = _minimum;
        if (possibleCards.size() < minimum)
            minimum = possibleCards.size();

        if (possibleCards.size() == minimum) {
            cardsSelected(game, possibleCards);
        } else {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new ArbitraryCardsSelectionDecision(1, _choiceText, possibleCards, _minimum, _maximum) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            cardsSelected(game, getSelectedCardsByResponse(result));
                        }
                    });
        }

        return new FullEffectResult(null, success, success);
    }

    protected abstract void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards);
}
