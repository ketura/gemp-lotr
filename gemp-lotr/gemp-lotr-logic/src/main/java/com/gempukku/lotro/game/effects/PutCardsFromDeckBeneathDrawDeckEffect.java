package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.game.actions.lotronly.SubAction;
import com.gempukku.lotro.game.actions.Action;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PutCardsFromDeckBeneathDrawDeckEffect extends AbstractSubActionEffect {
    private final Action _action;
    private final PhysicalCard _source;
    private final String _playerId;
    private final Set<PhysicalCard> _cards;

    private final boolean _reveal;

    public PutCardsFromDeckBeneathDrawDeckEffect(Action action, PhysicalCard source, String playerId, Collection<? extends PhysicalCard> cards, boolean reveal) {
        _action = action;
        _source = source;
        _playerId = playerId;
        _cards = new HashSet<>(cards);
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
        return true;
    }

    @Override
    public void playEffect(DefaultGame game) {
        SubAction subAction = new SubAction(_action);
        subAction.appendEffect(
                new ChooseAndPutNextCardFromDeckOnBottomOfDeck(subAction, _cards));
        processSubAction(game, subAction);
    }

    private class ChooseAndPutNextCardFromDeckOnBottomOfDeck extends ChooseArbitraryCardsEffect {
        private final Collection<PhysicalCard> _remainingCards;
        private final CostToEffectAction _subAction;

        public ChooseAndPutNextCardFromDeckOnBottomOfDeck(CostToEffectAction subAction, Collection<PhysicalCard> remainingCards) {
            super(_playerId, "Choose a card to put on bottom of your deck", remainingCards, 1, 1);
            _subAction = subAction;
            _remainingCards = remainingCards;
        }

        @Override
        protected void cardsSelected(DefaultGame game, Collection<PhysicalCard> selectedCards) {
            for (PhysicalCard selectedCard : selectedCards) {
                _subAction.appendEffect(
                        new PutCardFromDeckOnBottomOfDeckEffect(_source, selectedCard, _reveal));
                _remainingCards.remove(selectedCard);
                if (_remainingCards.size() > 0)
                    _subAction.appendEffect(
                            new ChooseAndPutNextCardFromDeckOnBottomOfDeck(_subAction, _remainingCards));
            }
        }
    }
}
