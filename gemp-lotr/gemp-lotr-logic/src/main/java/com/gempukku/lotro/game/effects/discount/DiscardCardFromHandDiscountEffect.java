package com.gempukku.lotro.game.effects.discount;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.game.effects.DiscountEffect;
import com.gempukku.lotro.game.actions.lotronly.SubAction;
import com.gempukku.lotro.game.effects.AbstractSubActionEffect;
import com.gempukku.lotro.game.actions.Action;

import java.util.Collection;

public class DiscardCardFromHandDiscountEffect extends AbstractSubActionEffect implements DiscountEffect {
    private final Action _action;
    private final String _playerId;
    private int _minimalDiscount;
    private int _discardedCount;
    private final Filterable[] _discardedCardFilter;

    public DiscardCardFromHandDiscountEffect(Action action, String playerId, Filterable... discardedCardFilter) {
        _action = action;
        _playerId = playerId;
        _discardedCardFilter = discardedCardFilter;
    }

    @Override
    public int getDiscountPaidFor() {
        return _discardedCount;
    }

    @Override
    public void setMinimalRequiredDiscount(int minimalDiscount) {
        _minimalDiscount = minimalDiscount;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Discard cards to reduce twilight cost";
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public int getMaximumPossibleDiscount(DefaultGame game) {
        return Filters.filter(game.getGameState().getHand(_playerId), game, _discardedCardFilter).size();
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return Filters.filter(game.getGameState().getHand(_playerId), game, _discardedCardFilter).size() >= _minimalDiscount;
    }

    @Override
    public void playEffect(DefaultGame game) {
        if (isPlayableInFull(game)) {
            SubAction subAction = new SubAction(_action);
            subAction.appendEffect(
                    new ChooseAndDiscardCardsFromHandEffect(_action, _playerId, false, _minimalDiscount, Integer.MAX_VALUE, _discardedCardFilter) {
                        @Override
                        protected void cardsBeingDiscardedCallback(Collection<PhysicalCard> cardsBeingDiscarded) {
                            _discardedCount = cardsBeingDiscarded.size();
                            discountPaidCallback(_discardedCount);
                        }
                    });
            processSubAction(game, subAction);
        }
    }

    protected void discountPaidCallback(int paid) {  }
}
