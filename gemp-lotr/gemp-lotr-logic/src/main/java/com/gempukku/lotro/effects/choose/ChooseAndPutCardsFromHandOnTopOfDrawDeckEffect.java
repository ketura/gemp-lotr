package com.gempukku.lotro.effects.choose;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.effects.PutCardFromHandOnTopOfDeckEffect;
import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.actions.lotronly.SubAction;
import com.gempukku.lotro.effects.AbstractSubActionEffect;
import com.gempukku.lotro.actions.Action;

import java.util.Collection;

public class ChooseAndPutCardsFromHandOnTopOfDrawDeckEffect extends AbstractSubActionEffect {
    private final Action _action;
    private final String _playerId;
    private final int _minimum;
    private final int _maximum;
    private final Filterable[] _filters;

    private final boolean _reveal;

    public ChooseAndPutCardsFromHandOnTopOfDrawDeckEffect(Action action, String playerId, int minimum, int maximum, boolean reveal, Filterable... filters) {
        _action = action;
        _playerId = playerId;
        _minimum = minimum;
        _maximum = maximum;
        _filters = filters;
        _reveal = reveal;
    }

    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return Filters.filter(game.getGameState().getHand(_playerId), game, _filters).size() >= _minimum;
    }

    @Override
    public void playEffect(DefaultGame game) {
        final Collection<LotroPhysicalCard> cards = Filters.filter(game.getGameState().getHand(_playerId), game, _filters);
        SubAction subAction = new SubAction(_action);
        subAction.appendEffect(
                new ChooseAndPutNextCardFromHandOnTopOfLibrary(subAction, Math.min(_minimum, cards.size()), Math.min(_maximum, cards.size()), cards));
        processSubAction(game, subAction);
    }

    private class ChooseAndPutNextCardFromHandOnTopOfLibrary extends ChooseArbitraryCardsEffect {
        private final Collection<LotroPhysicalCard> _remainingCards;
        private final CostToEffectAction _subAction;
        private final int _remainingMinCount;
        private final int _remainingMaxCount;

        public ChooseAndPutNextCardFromHandOnTopOfLibrary(CostToEffectAction subAction, int remainingMinCount, int remainingMaxCount, Collection<LotroPhysicalCard> remainingCards) {
            super(_playerId, "Choose a card to put on top of your deck", remainingCards, (remainingMinCount > 0) ? 1 : 0, 1);
            _subAction = subAction;
            _remainingMinCount = remainingMinCount;
            _remainingMaxCount = remainingMaxCount;
            _remainingCards = remainingCards;
        }

        @Override
        protected void cardsSelected(DefaultGame game, Collection<LotroPhysicalCard> selectedCards) {
            for (LotroPhysicalCard selectedCard : selectedCards) {
                _subAction.appendEffect(
                        new PutCardFromHandOnTopOfDeckEffect(selectedCard, _reveal));
                _remainingCards.remove(selectedCard);
                if (_remainingMaxCount - 1 > 0)
                    _subAction.appendEffect(
                            new ChooseAndPutNextCardFromHandOnTopOfLibrary(_subAction, _remainingMinCount-1, _remainingMaxCount - 1, _remainingCards));
            }
        }
    }
}
