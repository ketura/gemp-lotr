package com.gempukku.lotro.cards.set13.orc;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.discount.RemoveCardsFromDiscardDiscountEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

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
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        if (PlayConditions.canRemoveFromDiscard(self, game, playerId, 4, Culture.ORC))
            twilightModifier -= 1000;
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.setDiscountEffect(
                new RemoveCardsFromDiscardDiscountEffect(self, playerId, 4, Culture.ORC));
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Filters.owner(playerId), Culture.ORC, Keyword.LURKER, CardType.MINION));
        return action;
    }
}
