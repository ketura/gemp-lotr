package com.gempukku.lotro.cards.set4.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.PlaySiteEffect;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Fellowship: Add 2 burdens to play the fellowship's next 2 sites (replacing opponent's sites if necessary).
 */
public class Card4_304 extends AbstractEvent {
    public Card4_304() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "Get On and Get Away");
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        int siteNumber = game.getGameState().getCurrentSiteNumber();
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new AddBurdenEffect(self, 2));
        action.appendEffect(
                new PlaySiteEffect(playerId, null, siteNumber + 1));
        if (siteNumber < 8)
            action.appendEffect(
                    new PlaySiteEffect(playerId, null, siteNumber + 2));
        return action;
    }
}
