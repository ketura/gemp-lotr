package com.gempukku.lotro.cards.effects.discount;

import com.gempukku.lotro.cards.effects.DiscountEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.timing.AbstractSubActionEffect;
import com.gempukku.lotro.logic.timing.Action;

public class ExertCharactersDiscountEffect extends AbstractSubActionEffect implements DiscountEffect {
    private Action _action;
    private PhysicalCard _payingFor;
    private String _ownerId;
    private int _multiplier;
    private Filterable[] _exertFilter;
    private int _minimalDiscount;

    private int _exertedCount;

    public ExertCharactersDiscountEffect(Action action, PhysicalCard payingFor, String ownerId, int multiplier, Filterable... exertFilter) {
        _action = action;
        _payingFor = payingFor;
        _ownerId = ownerId;
        _multiplier = multiplier;
        _exertFilter = exertFilter;
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
    public void setMinimalRequiredDiscount(int minimalDiscount) {
        _minimalDiscount = minimalDiscount;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return _minimalDiscount <= _multiplier * (Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.and(_exertFilter), Filters.character, Filters.canExert(_payingFor)));
    }

    @Override
    public void playEffect(final LotroGame game) {
        if (isPlayableInFull(game)) {
            int minimalExerts;
            if (_minimalDiscount == 0) {
                minimalExerts = 0;
            } else {
                minimalExerts = _minimalDiscount / _multiplier + ((_minimalDiscount % _multiplier > 0) ? 1 : 0);
            }

            SubAction subAction = new SubAction(_action);
            subAction.appendEffect(
                    new ChooseAndExertCharactersEffect(subAction, _ownerId, minimalExerts, Integer.MAX_VALUE, Filters.and(_exertFilter), Filters.character) {
                        @Override
                        protected void forEachCardExertedCallback(PhysicalCard character) {
                            _exertedCount++;
                        }
                    });
            processSubAction(game, subAction);
        }
    }

    @Override
    public int getDiscountPaidFor() {
        return _exertedCount * _multiplier;
    }

}
