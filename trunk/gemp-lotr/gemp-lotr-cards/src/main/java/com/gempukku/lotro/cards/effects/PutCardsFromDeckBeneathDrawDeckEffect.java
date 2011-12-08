package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.timing.AbstractSubActionEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PutCardsFromDeckBeneathDrawDeckEffect extends AbstractSubActionEffect {
    private Action _action;
    private PhysicalCard _source;
    private String _playerId;
    private Set<PhysicalCard> _cards;

    public PutCardsFromDeckBeneathDrawDeckEffect(Action action, PhysicalCard source, String playerId, Collection<? extends PhysicalCard> cards) {
        _action = action;
        _source = source;
        _playerId = playerId;
        _cards = new HashSet<PhysicalCard>(cards);
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }

    @Override
    public void playEffect(LotroGame game) {
        SubAction subAction = new SubAction(_action);
        subAction.appendEffect(
                new ChooseAndPutNextCardFromDeckOnBottomOfDeck(subAction, _cards));
        processSubAction(game, subAction);
    }

    private class ChooseAndPutNextCardFromDeckOnBottomOfDeck extends ChooseArbitraryCardsEffect {
        private Collection<PhysicalCard> _remainingCards;
        private SubAction _subAction;

        public ChooseAndPutNextCardFromDeckOnBottomOfDeck(SubAction subAction, Collection<PhysicalCard> remainingCards) {
            super(_playerId, "Choose a card to put on bottom of your deck", remainingCards, 1, 1);
            _subAction = subAction;
            _remainingCards = remainingCards;
        }

        @Override
        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
            for (PhysicalCard selectedCard : selectedCards) {
                _subAction.appendEffect(
                        new PutCardFromDeckOnBottomOfDeckEffect(_source, selectedCard));
                _remainingCards.remove(selectedCard);
                if (_remainingCards.size() > 0)
                    _subAction.appendEffect(
                            new ChooseAndPutNextCardFromDeckOnBottomOfDeck(_subAction, _remainingCards));
            }
        }
    }
}
