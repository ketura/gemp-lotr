package com.gempukku.lotro.cards.set5.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Regroup: Spot Sam and discard Smeagol or Gollum to remove 3 burdens.
 */
public class Card5_112 extends AbstractEvent {
    public Card5_112() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "No Help For It", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canSpot(game, Filters.sam)
                && PlayConditions.canDiscardFromPlay(self, game, Filters.gollumOrSmeagol);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.gollumOrSmeagol));
        action.appendEffect(
                new RemoveBurdenEffect(self));
        action.appendEffect(
                new RemoveBurdenEffect(self));
        action.appendEffect(
                new RemoveBurdenEffect(self));
        return action;
    }
}
