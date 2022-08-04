package com.gempukku.lotro.logic.effects.discount;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.DiscountEffect;
import com.gempukku.lotro.logic.effects.OptionalEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.AbstractSubActionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;

public class OptionalDiscardDiscountEffect extends AbstractSubActionEffect implements DiscountEffect {
    private final int _discount;
    private boolean _paid;
    private int _minimalDiscount;
    private final String _playerId;
    private final int _discardCount;
    private final Filterable[] _discardFilters;
    private final Action _action;

    public OptionalDiscardDiscountEffect(Action action, int discount, String playerId, int discardCount, Filterable... discardFilters) {
        _action = action;
        _discount = discount;
        _playerId = playerId;
        _discardCount = discardCount;
        _discardFilters = discardFilters;
    }

    @Override
    public int getDiscountPaidFor() {
        return _paid ? _discount : 0;
    }

    @Override
    public void setMinimalRequiredDiscount(int minimalDiscount) {
        _minimalDiscount = minimalDiscount;
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
        if (PlayConditions.canDiscardFromPlay(_action.getActionSource(), game, _discardCount, _discardFilters))
            return _minimalDiscount <= _discount;
        return _minimalDiscount == 0;
    }

    @Override
    public int getMaximumPossibleDiscount(LotroGame game) {
        return PlayConditions.canDiscardFromPlay(_action.getActionSource(), game, _discardCount, _discardFilters) ? _discount : 0;
    }

    @Override
    public void playEffect(LotroGame game) {
        if (isPlayableInFull(game)) {
            SubAction subAction = new SubAction(_action);
            if (PlayConditions.canDiscardFromPlay(_action.getActionSource(), game, _discardCount, _discardFilters))
                subAction.appendEffect(
                        new OptionalEffect(subAction, _playerId,
                                new ChooseAndDiscardCardsFromPlayEffect(subAction, _playerId, _discardCount, _discardCount, _discardFilters) {
                                    @Override
                                    protected void cardsToBeDiscardedCallback(Collection<PhysicalCard> cards) {
                                        if (cards.size() == _discardCount) {
                                            _paid = true;
                                            discountPaidCallback();
                                        }
                                    }
                                }));
            processSubAction(game, subAction);
        }
    }

    protected void discountPaidCallback() {
    }
}
