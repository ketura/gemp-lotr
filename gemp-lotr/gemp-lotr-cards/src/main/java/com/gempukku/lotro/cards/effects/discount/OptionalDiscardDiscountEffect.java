package com.gempukku.lotro.cards.effects.discount;

import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.DiscountEffect;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.timing.AbstractSubActionEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;

public class OptionalDiscardDiscountEffect extends AbstractSubActionEffect implements DiscountEffect {
    private int _discount;
    private boolean _paid;
    private int _minimalDiscount;
    private String _playerId;
    private int _discardCount;
    private Filterable[] _discardFilters;
    private Action _action;

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
