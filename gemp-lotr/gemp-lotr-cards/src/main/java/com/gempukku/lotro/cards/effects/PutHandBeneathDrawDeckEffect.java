package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.cards.effects.choose.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.timing.AbstractSubActionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PutHandBeneathDrawDeckEffect extends AbstractSubActionEffect {
    private Action _action;
    private String _playerId;

    public PutHandBeneathDrawDeckEffect(Action action, String playerId) {
        _action = action;
        _playerId = playerId;
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
    public Collection<? extends EffectResult> playEffect(LotroGame game) {
        final Set<PhysicalCard> hand = new HashSet<PhysicalCard>(game.getGameState().getHand(_playerId));

        SubAction subAction = new SubAction(_action);
        subAction.appendEffect(
                new ChooseAndPutNextCardFromHandOnBottomOfLibrary(subAction, hand));
        processSubAction(game, subAction);

        return null;
    }

    private class ChooseAndPutNextCardFromHandOnBottomOfLibrary extends ChooseArbitraryCardsEffect {
        private Set<PhysicalCard> _remainingCards;
        private SubAction _subAction;

        public ChooseAndPutNextCardFromHandOnBottomOfLibrary(SubAction subAction, Set<PhysicalCard> remainingCards) {
            super(_playerId, "Choose a card to put on bottom of your deck", remainingCards, 1, 1);
            _subAction = subAction;
            _remainingCards = remainingCards;
        }

        @Override
        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
            for (PhysicalCard selectedCard : selectedCards) {
                _subAction.appendEffect(
                        new PutCardFromHandOnBottomOfDeckEffect(selectedCard));
                _remainingCards.remove(selectedCard);
                if (_remainingCards.size() > 0)
                    _subAction.appendEffect(
                            new ChooseAndPutNextCardFromHandOnBottomOfLibrary(_subAction, _remainingCards));
            }
        }
    }
}
