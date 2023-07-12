package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public abstract class ChooseArbitraryCardsEffect extends AbstractEffect {
    private final String _playerId;
    private final String _choiceText;
    private final boolean _showMatchingOnly;
    private final Collection<PhysicalCard> _cards;
    private final Filterable _filter;
    private final int _minimum;
    private final int _maximum;

    public ChooseArbitraryCardsEffect(String playerId, String choiceText, Collection<? extends PhysicalCard> cards, int minimum, int maximum) {
        this(playerId, choiceText, cards, Filters.any, minimum, maximum);
    }

    public ChooseArbitraryCardsEffect(String playerId, String choiceText, Collection<? extends PhysicalCard> cards, Filterable filter, int minimum, int maximum) {
        this(playerId, choiceText, cards, filter, minimum, maximum, false);
    }

    public ChooseArbitraryCardsEffect(String playerId, String choiceText, Collection<? extends PhysicalCard> cards, Filterable filter, int minimum, int maximum, boolean showMatchingOnly) {
        _playerId = playerId;
        _choiceText = choiceText;
        _showMatchingOnly = showMatchingOnly;
        _cards = new HashSet<>(cards);
        _filter = filter;
        _minimum = minimum;
        _maximum = maximum;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return Filters.filter(_cards, game, _filter).size() >= _minimum;
    }

    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(final DefaultGame game) {
        Collection<PhysicalCard> possibleCards = Filters.filter(_cards, game, _filter);

        boolean success = possibleCards.size() >= _minimum;

        int minimum = _minimum;

        if (possibleCards.size() < minimum)
            minimum = possibleCards.size();

        if (_maximum == 0) {
            cardsSelected(game, Collections.emptySet());
        } else if (possibleCards.size() == minimum) {
            cardsSelected(game, possibleCards);
        } else {
            Collection<PhysicalCard> toShow = _cards;
            if (_showMatchingOnly)
                toShow = possibleCards;

            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new ArbitraryCardsSelectionDecision(1, _choiceText, toShow, possibleCards, _minimum, _maximum) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            cardsSelected(game, getSelectedCardsByResponse(result));
                        }
                    });
        }

        return new FullEffectResult(success);
    }

    protected abstract void cardsSelected(DefaultGame game, Collection<PhysicalCard> selectedCards);
}
