package com.gempukku.lotro.cards.set13.men;

import com.gempukku.lotro.cards.effects.DiscountEffect;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.YesNoDecision;
import com.gempukku.lotro.logic.timing.AbstractSuccessfulEffect;

public class DiscountChoiceEffect extends AbstractSuccessfulEffect implements DiscountEffect {
    private String _playerId;
    private int _discountOffer;

    private int _minimalRequiredDiscount;
    private int _discountedBy = 0;

    public DiscountChoiceEffect(String playerId, int discountOffer) {
        _playerId = playerId;
        _discountOffer = discountOffer;
    }

    @Override
    public void setMinimalRequiredDiscount(int minimalDiscount) {
        _minimalRequiredDiscount = minimalDiscount;
    }

    @Override
    public int getDiscountPaidFor() {
        return _discountedBy;
    }

    @Override
    public String getText(LotroGame game) {
        return "Play card at a discount";
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public void playEffect(LotroGame game) {
        if (_minimalRequiredDiscount > 0)
            _discountedBy = _discountOffer;
        else {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new YesNoDecision("Do you want to play the card at -" + _discountOffer) {
                        @Override
                        protected void yes() {
                            _discountedBy = _discountOffer;
                        }
                    });
        }
    }
}
