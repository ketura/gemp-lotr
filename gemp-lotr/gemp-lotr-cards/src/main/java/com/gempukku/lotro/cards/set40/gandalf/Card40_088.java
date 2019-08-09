package com.gempukku.lotro.cards.set40.gandalf;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.PlaySiteEffect;

/**
 * Title: Speak "Friend" and Enter
 * Set: Second Edition
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event - Fellowship or Regroup
 * Card Number: 1C88
 * Game Text: Spot Gandalf to play the fellowship's next site (replacing opponent's site if necessary).
 * Draw a card if you play an underground site.
 */
public class Card40_088 extends AbstractEvent {
    public Card40_088() {
        super(Side.FREE_PEOPLE, 1, Culture.GANDALF, "Speak \"Friend\" and Enter", Phase.FELLOWSHIP, Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && Filters.canSpot(game, Filters.gandalf);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new PlaySiteEffect(action, playerId, null, game.getGameState().getCurrentSiteNumber() + 1) {
                    @Override
                    protected void sitePlayedCallback(PhysicalCard site) {
                        if (game.getModifiersQuerying().hasKeyword(game, site, Keyword.UNDERGROUND))
                            action.appendEffect(
                                    new DrawCardsEffect(action, playerId, 1));
                    }
                });
        return action;
    }
}
