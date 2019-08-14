package com.gempukku.lotro.cards.set13.wraith;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
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
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: You may remove from the game 4 [WRAITH] cards in your discard pile instead of paying the twilight cost for
 * this card. Make your Nazgul in a fierce skirmish strength +2.
 */
public class Card13_179 extends AbstractEvent {
    public Card13_179() {
        super(Side.SHADOW, 2, Culture.WRAITH, "From Hideous Eyrie", Phase.SKIRMISH);
    }

    @Override
    public int getPotentialDiscount(LotroGame game, String playerId, PhysicalCard self) {
        if (PlayConditions.canRemoveFromDiscard(self, game, playerId, 4, Culture.WRAITH))
            return 1000;
        return 0;
    }

    @Override
    public void appendPotentialDiscountEffects(LotroGame game, CostToEffectAction action, String playerId, PhysicalCard self) {
        action.appendPotentialDiscount(
                new RemoveCardsFromDiscardDiscountEffect(self, playerId, 4, 1000, Culture.WRAITH));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        if (game.getGameState().isFierceSkirmishes())
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Filters.owner(playerId), Race.NAZGUL));
        return action;
    }
}
