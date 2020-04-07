package com.gempukku.lotro.cards.set13.orc;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.effects.discount.RemoveCardsFromDiscardDiscountEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: You may remove from the game 4 [ORC] cards in your discard pile instead of paying the twilight cost for
 * this card. Make your [ORC] lurker minion strength +2.
 */
public class Card13_105 extends AbstractEvent {
    public Card13_105() {
        super(Side.SHADOW, 2, Culture.ORC, "Defiled", Phase.SKIRMISH);
    }

    @Override
    public int getPotentialDiscount(LotroGame game, String playerId, PhysicalCard self) {
        if (PlayConditions.canRemoveFromDiscard(self, game, playerId, 4, Culture.ORC))
            return 1000;
        return 0;
    }

    @Override
    public void appendPotentialDiscountEffects(LotroGame game, CostToEffectAction action, String playerId, PhysicalCard self) {
        action.appendPotentialDiscount(
                new RemoveCardsFromDiscardDiscountEffect(self, playerId, 4, 1000, Culture.ORC));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Filters.owner(playerId), Culture.ORC, Keyword.LURKER, CardType.MINION));
        return action;
    }
}
