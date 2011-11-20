package com.gempukku.lotro.cards.set2.gandalf;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
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
 * Set: Mines of Moria
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Fellowship or Regroup: Spot Gandalf to play the fellowship's next site (replacing opponent's site if
 * necessary). Draw a card if you play an underground site.
 */
public class Card2_026 extends AbstractOldEvent {
    public Card2_026() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "Speak \"Friend\" and Enter", Phase.FELLOWSHIP, Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.gandalf);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        PhysicalCard nextSite = game.getGameState().getSite(game.getGameState().getCurrentSiteNumber() + 1);
        if (nextSite == null || !nextSite.getOwner().equals(playerId)) {
            action.appendEffect(
                    new PlaySiteEffect(playerId, null, game.getGameState().getCurrentSiteNumber() + 1) {
                        @Override
                        protected void sitePlayedCallback(PhysicalCard site) {
                            if (game.getModifiersQuerying().hasKeyword(game.getGameState(), site, Keyword.UNDERGROUND))
                                action.appendEffect(
                                        new DrawCardsEffect(playerId, 1));
                        }
                    });
        }
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }
}
