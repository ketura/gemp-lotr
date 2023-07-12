package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.decisions.CardsSelectionDecision;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public abstract class ChooseActiveCardsEffect extends AbstractEffect {
    private final PhysicalCard _source;
    private final String _playerId;
    private String _choiceText;
    private final int _minimum;
    private final int _maximum;
    private final Filterable[] _filters;

    private boolean _shortcut = true;

    public ChooseActiveCardsEffect(PhysicalCard source, String playerId, String choiceText, int minimum, int maximum, Filterable... filters) {
        _source = source;
        _playerId = playerId;
        _choiceText = choiceText;
        _minimum = minimum;
        _maximum = maximum;
        _filters = filters;
    }

    public void setUseShortcut(boolean shortcut) {
        _shortcut = shortcut;
    }

    public void setChoiceText(String choiceText) {
        _choiceText = choiceText;
    }

    protected Filter getExtraFilterForPlaying(DefaultGame game) {
        return Filters.any;
    }

    protected Filter getExtraFilterForPlayabilityCheck(DefaultGame game) {
        return getExtraFilterForPlaying(game);
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return Filters.countActive(game, Filters.and(_filters, getExtraFilterForPlayabilityCheck(game))) >= _minimum;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public String getText(DefaultGame game) {
        return _choiceText;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(final DefaultGame game) {
        final Collection<PhysicalCard> matchingCards = Filters.filterActive(game, Filters.and(_filters, getExtraFilterForPlaying(game)));
        // Lets get the count realistic
        int maximum = Math.min(_maximum, matchingCards.size());

        int minimum = _minimum;
        if (matchingCards.size() < minimum)
            minimum = matchingCards.size();

        if (_shortcut && maximum == 0) {
            cardsSelected(game, Collections.emptySet());
        } else if (_shortcut && matchingCards.size() == minimum) {
            if (_source != null && matchingCards.size() > 0)
                game.getGameState().cardAffectsCard(_playerId, _source, matchingCards);
            cardsSelected(game, matchingCards);
        } else {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new CardsSelectionDecision(1, _choiceText, matchingCards, minimum, maximum) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            Set<PhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                            validateChoice(game, selectedCards);
                            if (_source != null && selectedCards.size() > 0)
                                game.getGameState().cardAffectsCard(_playerId, _source, selectedCards);
                            cardsSelected(game, selectedCards);
                        }
                    });
        }

        return new FullEffectResult(matchingCards.size() >= _minimum);
    }

    protected abstract void cardsSelected(DefaultGame game, Collection<PhysicalCard> cards);

    protected void validateChoice(DefaultGame game, Collection<PhysicalCard> cards) {

    }
}
